package com.BlogEcho.BlogEcho.service;

import com.BlogEcho.BlogEcho.dto.UserDto;
import com.BlogEcho.BlogEcho.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {


    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    List<UserDto> getUserByName(String name);
    void deleteUserById(Long id);
    void deleteUserByName(String name);

    List<String> getAllEmails();

    // Methods for OTP functionality
    void generateOtpAndSendEmail(String email);
    boolean verifyOtp(String email, String otp);

    String login(String email, String password);


    Optional<User> findById(Long id);

    UserDetails loadUserByUsername(String username);

}
