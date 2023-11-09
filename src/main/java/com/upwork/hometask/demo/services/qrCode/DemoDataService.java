package com.upwork.hometask.demo.services.qrCode;

import com.upwork.hometask.demo.domain.Classroom;
import com.upwork.hometask.demo.domain.Lecture;
import com.upwork.hometask.demo.domain.Schedule;
import com.upwork.hometask.demo.domain.Student;
import com.upwork.hometask.demo.repository.qrCode.ClassroomRepository;
import com.upwork.hometask.demo.repository.qrCode.LectureRepository;
import com.upwork.hometask.demo.repository.qrCode.ScheduleRepository;
import com.upwork.hometask.demo.repository.qrCode.StudentRepository;
import com.upwork.hometask.demo.resources.qrcode.model.ScheduleOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DemoDataService {

    private final LectureRepository lectureRepository;
    private final ScheduleRepository scheduleRepository;
    private final ClassroomRepository classroomRepository;
    private final StudentRepository studentRepository;

    public ScheduleOutput getData() {

        ScheduleOutput output = new ScheduleOutput();
        List<Student> all = studentRepository.findAll();
        for (Student student : all) {
            ScheduleOutput.Student studentOutput = new ScheduleOutput.Student();
            studentOutput.setStudentId(student.getId());
            studentOutput.setName(student.getName());
            output.getStudents().add(studentOutput);
        }

        List<Classroom> classrooms = classroomRepository.findAll();
        for (Classroom classroom : classrooms) {
            ScheduleOutput.Classroom classroomOutput = new ScheduleOutput.Classroom();
            classroomOutput.setClassroomId(classroom.getId());
            classroomOutput.setName(classroom.getName());
            classroomOutput.setQrCode(classroom.getQrCode());
            List<Schedule> schedules = scheduleRepository.findAllByClassroom(classroom);
            for (Schedule schedule : schedules) {
                ScheduleOutput.Schedule scheduleOutput = new ScheduleOutput.Schedule();
                scheduleOutput.setScheduleId(schedule.getId());
                scheduleOutput.setLecture(schedule.getLecture().getName());
                scheduleOutput.setStartTime(schedule.getStartTime());
                scheduleOutput.setEndTime(schedule.getEndTime());
                classroomOutput.getSchedules().add(scheduleOutput);
            }
            output.getClassrooms().add(classroomOutput);
        }
        return output;
    }

    public void create() {
        List<Classroom> classrooms = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Classroom classroom = new Classroom();
            classroom.setLatitude(1D);
            classroom.setLongitude(1D);
            classroom.setName("Class " + i);
            classroom.setQrCode(UUID.randomUUID().toString());
            classroomRepository.save(classroom);
            classrooms.add(classroom);

            int hour = LocalDateTime.now().getHour();

            if (hour > 5) {
                hour = hour - 3;
            }

            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

            for (int j = hour; j < (hour + 7); j++) {
                Lecture lecture = new Lecture();
                lecture.setName("Lecture (" + i + "," + j + ")");
                lectureRepository.save(lecture);

                Schedule schedule = new Schedule();
                schedule.setClassroom(classroom);
                schedule.setLecture(lecture);

                if (j >= 24) {
                    LocalDateTime startDate = now.withHour(j - 24);
                    startDate = startDate.plusDays(1);
                    LocalDateTime endDate = startDate.withMinute(50);
                    schedule.setStartTime(startDate);
                    schedule.setEndTime(endDate);
                } else {
                    LocalDateTime startDate = now.withHour(j);
                    LocalDateTime endDate = startDate.withMinute(50);
                    schedule.setStartTime(startDate);
                    schedule.setEndTime(endDate);
                }


                scheduleRepository.save(schedule);

            }
        }
        for (int i = 1; i <= 5; i++) {
            Student student = new Student();
            student.setName("Student " + i);
            studentRepository.save(student);
        }

    }


}
