package com.upwork.hometask.demo;

import com.upwork.hometask.demo.domain.Classroom;
import com.upwork.hometask.demo.domain.Lecture;
import com.upwork.hometask.demo.domain.Schedule;
import com.upwork.hometask.demo.domain.Student;
import com.upwork.hometask.demo.repository.qrCode.AttendanceTransactionRepository;
import com.upwork.hometask.demo.repository.qrCode.ScheduleRepository;
import com.upwork.hometask.demo.repository.qrCode.StudentRepository;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityOutput;
import com.upwork.hometask.demo.services.qrCode.CurrentActivityService;
import com.upwork.hometask.demo.services.qrCode.EncryptService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CurrentActivityServiceTest implements WithAssertions {

    @InjectMocks
    private CurrentActivityService currentActivityService;

    @Mock
    private  ScheduleRepository scheduleRepository;

    @Mock
    private AttendanceTransactionRepository attendanceTransactionRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private StudentRepository studentRepository;

    private List<Schedule> schedules = new ArrayList<>();

    private Student student ;
    private Optional<Student> studentOptional ;

    @BeforeEach
    public void setup() {

        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("Class1" );
        classroom.setQrCode(UUID.randomUUID().toString());

        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setName("Lecture1");

        Schedule schedule  =new Schedule();
        schedule.setId(1L);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.withHour(9);
        LocalDateTime endDate = startDate.withMinute(50);
        schedule.setStartTime(now);
        schedule.setEndTime(endDate);
        schedule.setClassroom(classroom);
        schedule.setLecture(lecture);

        schedules.add(schedule);

        student = new Student();
        student.setId(1L);
        student.setName("Student");

        studentOptional = Optional.of(student);
    }

    @Test
    void readQrCode_thenSuccess() {
        when(scheduleRepository.findAllByClassroomQrCodeAndStartTimeBetween(any(),any(),any())).thenReturn(schedules);

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");

        CurrentActivityInput input = new CurrentActivityInput();
        input.setQrCode("abc");
        input.setStudentId(1L);
        CurrentActivityOutput output = currentActivityService.listCurrentActivity(input);
        Assertions.assertEquals(1, output.getActivities().size());
        Assertions.assertEquals("xyz",output.getCorrelationID() );
    }
}