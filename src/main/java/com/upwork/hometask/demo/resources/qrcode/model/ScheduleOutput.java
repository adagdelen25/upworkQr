package com.upwork.hometask.demo.resources.qrcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleOutput {

    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private List<Classroom> classrooms = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    @Getter
    @Setter
    public static class Student {
        private Long studentId;
        private String name;
    }

    @Getter
    @Setter
    public static class Classroom {
        private String name;
        private Long classroomId;
        private String qrCode;
        List<Schedule> schedules = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Schedule {
        private Long scheduleId;
        private String lecture;
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = dateTimeFormat )
        private LocalDateTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = dateTimeFormat )
        private LocalDateTime endTime;
    }

}
