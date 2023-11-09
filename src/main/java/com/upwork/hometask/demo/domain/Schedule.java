package com.upwork.hometask.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
public class Schedule extends AuditDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Lecture lecture;

  @ManyToOne
  private Classroom classroom;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

}
