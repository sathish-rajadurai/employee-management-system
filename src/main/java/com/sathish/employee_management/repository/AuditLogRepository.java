package com.sathish.employee_management.repository;

import com.sathish.employee_management.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
