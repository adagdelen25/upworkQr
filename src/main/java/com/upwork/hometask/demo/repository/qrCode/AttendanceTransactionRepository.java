package com.upwork.hometask.demo.repository.qrCode;

import com.upwork.hometask.demo.domain.AttendanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AttendanceTransactionRepository extends JpaRepository<AttendanceTransaction, Long>, JpaSpecificationExecutor<AttendanceTransaction> {
    Optional<AttendanceTransaction> findByCorrelationID(String correlationID);
}