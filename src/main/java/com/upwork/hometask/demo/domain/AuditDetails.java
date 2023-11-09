package com.upwork.hometask.demo.domain;

import com.upwork.hometask.demo.domain.audit.AuditListener;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public class AuditDetails implements Serializable {

    @CreatedDate
    @Column(nullable = false)
    @NotNull(message = "Created at field is required")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @CreatedBy
    @Column(nullable = false)
    @NotBlank(message = "Created by field is required")
    private String createdBy = "SYSTEM";

    @LastModifiedDate
    private OffsetDateTime modifiedAt;
    @LastModifiedBy
    private String modifiedBy;

    @Column(nullable = false)
    @NotNull(message = "isDeleted field is required")
    private Boolean isDeleted = false;
}