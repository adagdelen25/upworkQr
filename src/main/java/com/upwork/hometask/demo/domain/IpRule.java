package com.upwork.hometask.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(
    indexes = {
        @Index(columnList = "sourceStartValue, sourceEndValue"),
        @Index(columnList = "destinationStartValue,destinationEndValue")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"specificName"})
    }
)
public class IpRule extends AuditDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String specificName;

  private String sourceStart;
  private Long sourceStartValue;
  private String sourceEnd;
  private Long sourceEndValue;

  private String destinationStart;
  private Long destinationStartValue;
  private String destinationEnd;
  private Long destinationEndValue;

  private Boolean allow;
  private Boolean isSubnet = false;
  private String subnetSource;
  private String subnetDestination;

}
