package com.sathish.employee_management.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", schema = "employee_db")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String action;

    private String entityName;

    private Long entityId;

    private LocalDateTime timestamp;

    public AuditLog() {
    }

    public AuditLog(String username, String action, String entityName, Long entityId, LocalDateTime timestamp) {
        this.username = username;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getEntityName() {
        return entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
