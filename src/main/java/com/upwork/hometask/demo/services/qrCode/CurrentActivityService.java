package com.upwork.hometask.demo.services.qrCode;

import com.upwork.hometask.demo.domain.AttendanceTransaction;
import com.upwork.hometask.demo.domain.Schedule;
import com.upwork.hometask.demo.domain.Student;
import com.upwork.hometask.demo.models.enums.AttendanceTransactionStatus;
import com.upwork.hometask.demo.repository.qrCode.AttendanceTransactionRepository;
import com.upwork.hometask.demo.repository.qrCode.ScheduleRepository;
import com.upwork.hometask.demo.repository.qrCode.StudentRepository;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrentActivityService {

    private static final int TIME_INTERVAL = 30;

    private final ScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;
    private final EncryptService encryptService;

    private final AttendanceTransactionRepository attendanceTransactionRepository;

    public CurrentActivityOutput listCurrentActivity(CurrentActivityInput input) {
        String qrCode = input.getQrCode();
        CurrentActivityOutput output = new CurrentActivityOutput();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMinutes(TIME_INTERVAL);
        LocalDateTime to = now.plusMinutes(TIME_INTERVAL);

        List<Schedule> schedules = scheduleRepository.findAllByClassroomQrCodeAndStartTimeBetween(qrCode, from, to);
//        List<Schedule> schedules = scheduleRepository.findAllByClassroomQrCode(qrCode);
        for (Schedule schedule : schedules) {
            CurrentActivityOutput.CurrentActivity currentActivity = new CurrentActivityOutput.CurrentActivity();
            currentActivity.setClassroom(schedule.getClassroom().getName());
            currentActivity.setLecture(schedule.getLecture().getName());
            currentActivity.setScheduleId(schedule.getId());
            currentActivity.setStartTime(schedule.getStartTime());
            currentActivity.setEndTime(schedule.getEndTime());
            output.getActivities().add(currentActivity);
        }

        Long studentId = input.getStudentId();

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + studentId + " in Student"));

        AttendanceTransaction attendanceTransaction = new AttendanceTransaction();
        attendanceTransaction.setStudent(student);
        attendanceTransaction.setReadAt(LocalDateTime.now());
        attendanceTransaction.setStatus(AttendanceTransactionStatus.READ);
        String correlationID = UUID.randomUUID().toString();
        attendanceTransaction.setCorrelationID(correlationID);
        //todo an algorithm create an verification code using unique student
        attendanceTransaction.setVerificationCode(123456);
        attendanceTransactionRepository.save(attendanceTransaction);
        String encrypt = encryptService.encrypt(correlationID);
        output.setCorrelationID(encrypt);
        return output;
    }

}
