package com.upwork.hometask.demo.services.qrCode;

import com.upwork.hometask.demo.domain.AttendanceTransaction;
import com.upwork.hometask.demo.domain.Classroom;
import com.upwork.hometask.demo.domain.Schedule;
import com.upwork.hometask.demo.domain.Student;
import com.upwork.hometask.demo.models.enums.AttendanceTransactionStatus;
import com.upwork.hometask.demo.models.exception.AlreadyExistsException;
import com.upwork.hometask.demo.models.exception.InvalidITokenException;
import com.upwork.hometask.demo.models.exception.TimeExpiredTokenException;
import com.upwork.hometask.demo.repository.qrCode.AttendanceTransactionRepository;
import com.upwork.hometask.demo.repository.qrCode.ScheduleRepository;
import com.upwork.hometask.demo.repository.qrCode.StudentRepository;
import com.upwork.hometask.demo.resources.qrcode.model.CheckInInput;
import com.upwork.hometask.demo.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckInService {

    private final ScheduleRepository scheduleRepository;
    private final StudentAttendanceService studentAttendanceService;
    private final AttendanceTransactionRepository attendanceTransactionRepository;
    private final StudentRepository studentRepository;
    private final EncryptService encryptService;


    private static final int INTERVAL_TIME = 30;

    private static final double MAX_DISTANCE_IN_METER = 10D;

    private static final boolean CHECK_DISTANCE = false;

    public void checkIn(CheckInInput input) {

        Long scheduleId = input.getScheduleId();
        Long studentId = input.getStudentId();
        String correlationIDEncrypt = input.getCorrelationID();
        String correlationID = encryptService.decrypt(correlationIDEncrypt);

        AttendanceTransaction attendanceTransaction = attendanceTransactionRepository.findByCorrelationID(correlationID).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + scheduleId + " in Schedule"));

        if (attendanceTransaction.getStatus() != AttendanceTransactionStatus.READ) {
            throw new InvalidITokenException();
        }

        if (!Objects.equals(attendanceTransaction.getVerificationCode(), input.getVerificationCode())) {
            throw new InvalidITokenException();
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + scheduleId + " in Schedule"));

        Classroom classroom = schedule.getClassroom();

        if (CHECK_DISTANCE) {
            double m = MapUtils.distance(classroom.getLatitude(), classroom.getLongitude(), input.getLatitude(), input.getLongitude(), "M");
            if (m > MAX_DISTANCE_IN_METER) {
                throw new InvalidITokenException();
            }
        }

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + studentId + " in Student"));


        if (studentAttendanceService.hasCurrentAttendance(student, schedule)) {
            throw new AlreadyExistsException("You are already checked this schedule");
        }

        if (studentAttendanceService.hasCurrentAttendance(studentId)) {
            throw new AlreadyExistsException("You  already attendanced another lecture");
        }

        if (!Objects.equals(attendanceTransaction.getStudent().getId(), student.getId())) {
            throw new InvalidITokenException();
        }

        LocalDateTime readAt = attendanceTransaction.getReadAt();
        LocalDateTime maxAvailable = readAt.plusSeconds(INTERVAL_TIME);
        if (maxAvailable.isBefore(LocalDateTime.now())) {
            log.error("{} correlationID was tried after maxAvailable: {} ", correlationID, maxAvailable);
            attendanceTransaction.setStatus(AttendanceTransactionStatus.EXPIRED);
            attendanceTransactionRepository.save(attendanceTransaction);
            throw new TimeExpiredTokenException();
        }

        studentAttendanceService.addAttendance(student, schedule);

        attendanceTransaction.setStatus(AttendanceTransactionStatus.CHECK_IN);
        attendanceTransactionRepository.save(attendanceTransaction);

        log.info("studentId : {} is successfully checkin schedule {}", studentId, scheduleId);
    }

}
