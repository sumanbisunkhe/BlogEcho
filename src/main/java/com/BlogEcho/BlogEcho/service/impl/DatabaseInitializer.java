package com.BlogEcho.BlogEcho.service.impl;


import com.BlogEcho.BlogEcho.enums.Gender;
import com.BlogEcho.BlogEcho.enums.Role;
import com.BlogEcho.BlogEcho.model.User;
import com.BlogEcho.BlogEcho.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer {

    @Autowired
    private UserRepo userRepository;

    @PostConstruct
    public void init() {
        if (!userRepository.existsByEmail("betovak592@cartep.com")) {
            User user = new User();
            user.setUsername("Suman Bisunkhe");
            user.setPassword("$2a$12$fIWpiW3lvJzUx5xmdQY4mukZsgZFDfsQNeaFF7XaSsOhx5axBdZja");
            user.setEmail("betovak592@cartep.com");
            user.setCountry("Nepal");
            user.setAge(21);
            user.setGender(Gender.Male);
            user.setRole(Role.Admin);
            user.setEnabled(true);
            user.setOtpCode("123456");
            user.setOtpExpiryTime(null);
            userRepository.save(user);
        }
    }
}
