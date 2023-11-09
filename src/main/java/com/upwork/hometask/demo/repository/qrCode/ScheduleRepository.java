package com.upwork.hometask.demo.repository.qrCode;

import com.upwork.hometask.demo.domain.Classroom;
import com.upwork.hometask.demo.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {

    List<Schedule> findAllByClassroomQrCode(String qrCode);
    List<Schedule> findAllByClassroomQrCodeAndStartTimeBetween(String qrCode, LocalDateTime from, LocalDateTime to);

    List<Schedule> findAllByClassroom(Classroom classroom);

}