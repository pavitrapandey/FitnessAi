package com.fitUAI.userService.Exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("User Exists already");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
