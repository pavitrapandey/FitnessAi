package com.fitUAI.userService.Service;

import com.fitUAI.userService.Dto.RegisterRequest;
import com.fitUAI.userService.Dto.UserResponse;
import com.fitUAI.userService.Exception.UserAlreadyExistException;
import com.fitUAI.userService.Repository.UserRepo;
import com.fitUAI.userService.model.User;
import com.fitUAI.userService.model.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserResponse registerUser(RegisterRequest registerRequest) {
        User user = new User();
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            User existUser=userRepo.findByEmail(registerRequest.getEmail());
            UserResponse response=modelMapper.map(existUser, UserResponse.class);
            return response;
        }
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setKeyCloakId(registerRequest.getKeycloakId());
        user.setPassword(registerRequest.getPassword());
        user.setRole(UserRole.USER);

        userRepo.save(user);


        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user= userRepo
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public Boolean isExist(String userId) {
        return userRepo.existsByKeyCloakId(userId);
    }
}
