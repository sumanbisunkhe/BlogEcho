package com.BlogEcho.BlogEcho.controller;

import com.BlogEcho.BlogEcho.dto.UserDto;
import com.BlogEcho.BlogEcho.service.impl.UserServiceImpl;
import com.BlogEcho.BlogEcho.util.AuthenticationRequest;
import com.BlogEcho.BlogEcho.util.AuthenticationResponse;
import com.BlogEcho.BlogEcho.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final UserServiceImpl userService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Validation error");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }



        UserDto createdUser = userService.createUser(userDto);

        if (userDto.getOtpCode() != null) {
            boolean isVerified = userService.verifyOtp(userDto.getEmail(), userDto.getOtpCode());
            if (isVerified) {
                Map<String, Object> response = Map.of(
                        "message", "Account " + userDto.getUsername() + " created and email verified successfully.",
                        "data", createdUser
                );
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                Map<String, Object> response = Map.of(
                        "message", "Invalid or expired OTP."
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        Map<String, Object> response = Map.of(
                "message", "Account " + userDto.getUsername() + " created successfully. Please verify your email with the OTP sent.",
                "data", createdUser
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        Map<String, Object> response = Map.of(
                "message", "Users list fetched successfully.",
                "data", users
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        Map<String, Object> response = Map.of(
                "message", "User fetched successfully.",
                "data", user
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Map<String, Object>> getUserByName(@PathVariable String username) {
        List<UserDto> users = userService.getUserByName(username);
        Map<String, Object> response = Map.of(
                "message", "Users fetched successfully.",
                "data", users
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        Map<String, Object> response = Map.of(
                "message", "User with ID " + id + " has been successfully deleted."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/username/{username}")
    public ResponseEntity<Map<String, Object>> deleteUserByName(@PathVariable String username) {
        userService.deleteUserByName(username);
        Map<String, Object> response = Map.of(
                "message", "Users with username " + username + " have been successfully deleted."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/emails")
    public ResponseEntity<Map<String, Object>> getAllEmails() {
        List<String> emails = userService.getAllEmails();
        Map<String, Object> response = Map.of(
                "message", "User emails fetched successfully.",
                "data", emails
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestParam String email) {
        userService.generateOtpAndSendEmail(email);
        Map<String, String> response = Map.of("message", "OTP generated and sent to email.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> otpRequest) {
        String email = otpRequest.get("email");
        String otp = otpRequest.get("otp_code");

        boolean isVerified = userService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully. User registration is complete.");
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.WWW_AUTHENTICATE, "OTP realm=\"example\"");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(headers)
                    .body("Invalid or expired OTP.");
        }
    }


}

