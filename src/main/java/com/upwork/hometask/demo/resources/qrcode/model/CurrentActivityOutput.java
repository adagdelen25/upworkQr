package com.upwork.hometask.demo.resources.qrcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CurrentActivityOutput {
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private List<CurrentActivity> activities = new ArrayList<>();

    private String correlationID;

    @Getter
    @Setter
    public static class CurrentActivity {
        private Long scheduleId;
        private String classroom;
        private String lecture;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = dateTimeFormat)
        private LocalDateTime startTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = dateTimeFormat)
        private LocalDateTime endTime;
    }
}
