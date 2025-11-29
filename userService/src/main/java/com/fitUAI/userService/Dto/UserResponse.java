package com.fitUAI.userService.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse{
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
