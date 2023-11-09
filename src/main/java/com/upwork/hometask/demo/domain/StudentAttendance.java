package com.upwork.hometask.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor

public class StudentAttendance extends AuditDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private LocalDateTime readAt;

    @ManyToOne
    private Schedule schedule;

    @ManyToOne
    private Student student;

}
