package com.fitUAI.userService.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
