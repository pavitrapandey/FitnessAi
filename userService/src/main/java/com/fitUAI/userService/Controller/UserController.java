package com.fitUAI.userService.Controller;

import com.fitUAI.userService.Dto.RegisterRequest;
import com.fitUAI.userService.Dto.UserResponse;
import com.fitUAI.userService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterRequest request)
    {
        UserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String user_id)
    {
        UserResponse response = userService.getUserById(user_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{user_id}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String user_id)
    {
        return new ResponseEntity<>(userService.isExist(user_id), HttpStatus.OK);
    }
}
