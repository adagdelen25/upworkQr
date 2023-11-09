package com.upwork.hometask.demo.services.qrCode;

import com.upwork.hometask.demo.domain.Schedule;
import com.upwork.hometask.demo.domain.Student;
import com.upwork.hometask.demo.domain.StudentAttendance;
import com.upwork.hometask.demo.models.exception.AlreadyExistsException;
import com.upwork.hometask.demo.repository.qrCode.StudentAttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentAttendanceService {

    private final StudentAttendanceRepository studentAttendanceRepository;

    public boolean hasCurrentAttendance(Long studentId) {
        List<StudentAttendance> studentAttendances = studentAttendanceRepository.listAttendanceSpecificTime(studentId, LocalDateTime.now());
        return !CollectionUtils.isEmpty(studentAttendances);
    }

    public boolean hasCurrentAttendance(Student student, Schedule schedule) {
        Optional<StudentAttendance> studentAttendanceOptional = studentAttendanceRepository.findByStudentAndSchedule(student, schedule);
        return studentAttendanceOptional.isPresent();
    }

    public void addAttendance(Student student, Schedule schedule) {
        StudentAttendance studentAttendance = new StudentAttendance();
        studentAttendance.setReadAt(LocalDateTime.now());
        studentAttendance.setStudent(student);
        studentAttendance.setSchedule(schedule);
        studentAttendanceRepository.save(studentAttendance);
    }


}
