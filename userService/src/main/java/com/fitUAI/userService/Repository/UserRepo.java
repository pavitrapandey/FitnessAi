package com.fitUAI.userService.Repository;

import com.fitUAI.userService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);
}
