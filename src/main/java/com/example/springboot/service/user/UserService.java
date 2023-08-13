package com.example.springboot.service.user;

import com.example.springboot.domain.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    User create (User user);
    User getByCredentials(String email, String password, PasswordEncoder encoder);

}
