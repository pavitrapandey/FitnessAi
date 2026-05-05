package com.fitUAI.userService.Dto;

import com.fitUAI.userService.model.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class RegisterRequest{
    private String name;
    private String keycloakId;
    private String email;
    private String password;
}
