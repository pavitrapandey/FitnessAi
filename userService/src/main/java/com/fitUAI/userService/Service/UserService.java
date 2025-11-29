package com.fitUAI.userService.Service;

import com.fitUAI.userService.Dto.RegisterRequest;
import com.fitUAI.userService.Dto.UserResponse;
import com.fitUAI.userService.model.User;

public interface UserService {
    UserResponse registerUser(RegisterRequest registerRequest);
    UserResponse getUserById(String id);

    Boolean isExist(String userId);
}
