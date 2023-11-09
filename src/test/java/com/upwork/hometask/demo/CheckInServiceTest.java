package com.upwork.hometask.demo;

import com.upwork.hometask.demo.domain.*;
import com.upwork.hometask.demo.models.enums.AttendanceTransactionStatus;
import com.upwork.hometask.demo.models.exception.*;
import com.upwork.hometask.demo.repository.qrCode.AttendanceTransactionRepository;
import com.upwork.hometask.demo.repository.qrCode.ScheduleRepository;
import com.upwork.hometask.demo.repository.qrCode.StudentRepository;
import com.upwork.hometask.demo.resources.qrcode.model.CheckInInput;
import com.upwork.hometask.demo.services.qrCode.CheckInService;
import com.upwork.hometask.demo.services.qrCode.DistanceService;
import com.upwork.hometask.demo.services.qrCode.EncryptService;
import com.upwork.hometask.demo.services.qrCode.StudentAttendanceService;
import org.assertj.core.api.WithAssertions;
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
class CheckInServiceTest implements WithAssertions {

    @InjectMocks
    private CheckInService checkInService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private AttendanceTransactionRepository attendanceTransactionRepository;
    @Mock
    private StudentAttendanceService studentAttendanceService;
    @Mock
    private DistanceService distanceService;

    @Mock
    private EncryptService encryptService;

    @Mock
    private StudentRepository studentRepository;

    private List<Schedule> schedules = new ArrayList<>();

    private Student student;
    private Optional<AttendanceTransaction> attendanceTransactionOptional;
    private Optional<Student> studentOptional;

    private Schedule schedule;
    private Optional<Schedule> scheduleOptional;

    @BeforeEach
    public void setup() {

        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("Class1");
        classroom.setQrCode(UUID.randomUUID().toString());

        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setName("Lecture1");

        schedule = new Schedule();
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
        scheduleOptional = Optional.of(schedule);

        AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
        attendanceTransaction.setStatus(AttendanceTransactionStatus.READ);
        attendanceTransaction.setStudent(student);
        attendanceTransaction.setCorrelationID("abc");
        attendanceTransaction.setId(1L);
        attendanceTransaction.setVerificationCode(123456);
        attendanceTransaction.setReadAt(LocalDateTime.now());
        attendanceTransactionOptional = Optional.of(attendanceTransaction);

    }

    @Test
    void readQrCode_thenSuccess() {

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(scheduleRepository.findById(any())).thenReturn(scheduleOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");
        when(encryptService.decrypt(any())).thenReturn("abc");
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(studentAttendanceService.hasCurrentAttendance(any(), any())).thenReturn(false);
        when(studentAttendanceService.hasCurrentAttendance(any())).thenReturn(false);
        when(distanceService.okDistance(any(), any(), any(), any())).thenReturn(true);

        CheckInInput checkInInput = new CheckInInput();
        checkInInput.setCorrelationID("xyz");
        checkInInput.setScheduleId(1L);
        checkInInput.setStudentId(1L);
        checkInInput.setVerificationCode(123456);

        checkInService.checkIn(checkInInput);

    }

    @Test
    void readQrCode_throwException_alreadyCheckin() {

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(scheduleRepository.findById(any())).thenReturn(scheduleOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");
        when(encryptService.decrypt(any())).thenReturn("abc");
        attendanceTransactionOptional.get().setStatus(AttendanceTransactionStatus.CHECK_IN);
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(studentAttendanceService.hasCurrentAttendance(any(), any())).thenReturn(true);
        when(distanceService.okDistance(any(), any(), any(), any())).thenReturn(true);

        CheckInInput checkInInput = new CheckInInput();
        checkInInput.setCorrelationID("xyz");
        checkInInput.setScheduleId(1L);
        checkInInput.setStudentId(1L);
        checkInInput.setVerificationCode(123456);

        org.junit.jupiter.api.Assertions.assertThrows(InvalidITokenException.class, () -> {
            checkInService.checkIn(checkInInput);
        });

    }

    @Test
    void readQrCode_throwException_verificationCode() {

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(scheduleRepository.findById(any())).thenReturn(scheduleOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");
        when(encryptService.decrypt(any())).thenReturn("abc");
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(studentAttendanceService.hasCurrentAttendance(any(), any())).thenReturn(true);
        when(distanceService.okDistance(any(), any(), any(), any())).thenReturn(true);

        CheckInInput checkInInput = new CheckInInput();
        checkInInput.setCorrelationID("xyz");
        checkInInput.setScheduleId(1L);
        checkInInput.setStudentId(1L);
        checkInInput.setVerificationCode(1234567);

        org.junit.jupiter.api.Assertions.assertThrows(VerificationCodeException.class, () -> {
            checkInService.checkIn(checkInInput);
        });
    }

    @Test
    void readQrCode_throwException_distance() {

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(scheduleRepository.findById(any())).thenReturn(scheduleOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");
        when(encryptService.decrypt(any())).thenReturn("abc");
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(studentAttendanceService.hasCurrentAttendance(any(), any())).thenReturn(true);
        when(distanceService.okDistance(any(), any(), any(), any())).thenReturn(false);
        CheckInInput checkInInput = new CheckInInput();
        checkInInput.setCorrelationID("xyz");
        checkInInput.setScheduleId(1L);
        checkInInput.setStudentId(1L);
        checkInInput.setLatitude(10000D);
        checkInInput.setLongitude(10000D);
        checkInInput.setVerificationCode(123456);

        org.junit.jupiter.api.Assertions.assertThrows(DistanceException.class, () -> {
            checkInService.checkIn(checkInInput);
        });

    }

    @Test
    void readQrCode_throwException_alreadyCheckinAnotherClassroom() {

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(scheduleRepository.findById(any())).thenReturn(scheduleOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");
        when(encryptService.decrypt(any())).thenReturn("abc");
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(studentAttendanceService.hasCurrentAttendance(any(), any())).thenReturn(true);
        when(distanceService.okDistance(any(), any(), any(), any())).thenReturn(true);

        CheckInInput checkInInput = new CheckInInput();
        checkInInput.setCorrelationID("xyz");
        checkInInput.setScheduleId(1L);
        checkInInput.setStudentId(1L);
        checkInInput.setVerificationCode(123456);

        org.junit.jupiter.api.Assertions.assertThrows(AlreadyExistsException.class, () -> {
            checkInService.checkIn(checkInInput);
        });

    }

    @Test
    void readQrCode_throwException_TimeExpired() {

        when(studentRepository.findById(any())).thenReturn(studentOptional);
        when(scheduleRepository.findById(any())).thenReturn(scheduleOptional);
        when(encryptService.encrypt(any())).thenReturn("xyz");
        when(encryptService.decrypt(any())).thenReturn("abc");
        attendanceTransactionOptional.get().setReadAt(LocalDateTime.now().minusMinutes(100));
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(attendanceTransactionRepository.findByCorrelationID(any())).thenReturn(attendanceTransactionOptional);
        when(studentAttendanceService.hasCurrentAttendance(any(), any())).thenReturn(false);
        when(studentAttendanceService.hasCurrentAttendance(any())).thenReturn(false);
        when(distanceService.okDistance(any(), any(), any(), any())).thenReturn(true);

        CheckInInput checkInInput = new CheckInInput();
        checkInInput.setCorrelationID("xyz");
        checkInInput.setScheduleId(1L);
        checkInInput.setStudentId(1L);
        checkInInput.setVerificationCode(123456);

        org.junit.jupiter.api.Assertions.assertThrows(TimeExpiredTokenException.class, () -> {
            checkInService.checkIn(checkInInput);
        });

    }

}