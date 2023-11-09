package com.upwork.hometask.demo.repository.qrCode;

import com.upwork.hometask.demo.domain.Schedule;
import com.upwork.hometask.demo.domain.Student;
import com.upwork.hometask.demo.domain.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long>, JpaSpecificationExecutor<StudentAttendance> {

    Optional<StudentAttendance> findByStudentAndSchedule(Student student, Schedule schedule);

    @Query("select e from StudentAttendance e where e.student.id =:studentId and  :specific between e.schedule.startTime and e.schedule.endTime")
    List<StudentAttendance> listAttendanceSpecificTime(@Param("studentId") Long studentId, @Param("specific")LocalDateTime specific);


}