package com.upwork.hometask.demo.domain;

import com.upwork.hometask.demo.models.enums.AttendanceTransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class AttendanceTransaction extends AuditDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String correlationID;

    private Integer verificationCode;

    @ManyToOne
    private Student student;

    private LocalDateTime readAt;

    @Enumerated(EnumType.STRING)
    private AttendanceTransactionStatus status;

}
