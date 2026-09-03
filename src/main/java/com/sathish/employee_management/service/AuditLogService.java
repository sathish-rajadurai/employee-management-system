package com.sathish.employee_management.service;

import com.sathish.employee_management.entity.AuditLog;
import com.sathish.employee_management.repository.AuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String entityName, Long entityId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "SYSTEM";

        AuditLog auditLog = new AuditLog(username, action, entityName, entityId, LocalDateTime.now());
        auditLogRepository.save(auditLog);
    }
}
