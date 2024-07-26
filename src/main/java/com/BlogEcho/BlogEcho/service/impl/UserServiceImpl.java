package com.BlogEcho.BlogEcho.service.impl;

import com.BlogEcho.BlogEcho.dto.EmailDto;
import com.BlogEcho.BlogEcho.dto.UserDto;
import com.BlogEcho.BlogEcho.enums.Role;
import com.BlogEcho.BlogEcho.exceptions.InvalidOtpException;
import com.BlogEcho.BlogEcho.exceptions.UserAlreadyExistsException;
import com.BlogEcho.BlogEcho.exceptions.UserNotFoundException;
import com.BlogEcho.BlogEcho.model.User;
import com.BlogEcho.BlogEcho.repo.UserRepo;
import com.BlogEcho.BlogEcho.service.EmailService;
import com.BlogEcho.BlogEcho.service.UserService;
import com.BlogEcho.BlogEcho.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
// Check if the email or username already exists
        Optional<User> existingUserByEmail = userRepo.findByEmail(userDto.getEmail());
        Optional<User> existingUserByUsername = userRepo.findByUsername(userDto.getUsername());

        if (existingUserByEmail.isPresent()) {
            User existingUser = existingUserByEmail.get();
            if (existingUser.isEnabled()) {
                throw new UserAlreadyExistsException("Email already exists.");
            } else {
                // If the user with the email exists but is not enabled, resend the OTP
                logger.info("User with email {} exists but is not enabled. Requesting OTP verification.", userDto.getEmail());
                sendOtpByEmail(existingUser.getEmail(), existingUser.getOtpCode());
                return convertToDto(existingUser);
            }
        }

        if (existingUserByUsername.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists.");
        }


        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setCountry(userDto.getCountry());
        user.setAge(userDto.getAge());
        user.setGender(userDto.getGender());
        user.setRole(Role.User); // Default role to USER
        String otp = generateOtp();
        user.setOtpCode(otp); // Generate OTP
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5)); // OTP expiry in 5 minutes
        user.setEnabled(false); // User is not enabled until OTP is verified

        User savedUser = userRepo.save(user);
        sendOtpByEmail(user.getEmail(), user.getOtpCode());

        // Log the OTP for debugging purposes (remove this in production)
        System.out.println("Generated OTP: " + otp + " for email: " + user.getEmail());

        return convertToDto(savedUser);
    }

    @Override
    public String login(String username, String password) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new UserNotFoundException("Invalid username or password");
        }

        User user = userOptional.get();
        if (!user.isEnabled()) {
            throw new InvalidOtpException("Account is not enabled. Please verify your OTP.");
        }

        return jwtUtil.generateToken(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepo.findAll();
        return userList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return convertToDto(user);
    }

    @Override
    public List<UserDto> getUserByName(String name) {
        List<User> users = userRepo.findByUsernameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            throw new UserNotFoundException("User not found with name: " + name);
        }
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        userRepo.delete(user);
    }

    @Override
    public void deleteUserByName(String name) {
        List<User> users = userRepo.findByUsernameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            throw new UserNotFoundException("User not found with username: " + name);
        }
        userRepo.deleteAll(users);
    }

    @Override
    public List<String> getAllEmails() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public void generateOtpAndSendEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5)); // OTP expiry in 5 minutes
        userRepo.save(user);

        sendOtpByEmail(email, otp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Check if OTP is expired or not set
        if (user.getOtpCode() == null || user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Check if OTP matches
        boolean isValid = user.getOtpCode().equals(otp);
        if (isValid) {
            // Clear OTP fields after successful verification
            user.setOtpCode(null);
            user.setOtpExpiryTime(null);
            user.setEnabled(true);
            userRepo.save(user);

            //Send welcome email
            String message = String.format("Hello %s,\n\nWelcome to BlogEcho, a vibrant platform where the boundaries of learning and sharing are limitless. Here, you'll find an endless opportunity to expand your knowledge and engage with a community eager to exchange insights and ideas.", user.getUsername());
            EmailDto emailDto = new EmailDto(user.getEmail(), "Welcome to BlogEcho", message);
            emailService.sendEmail(emailDto);
        }
        return isValid;
    }




    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(999999); // Generates a random number between 0 and 999999
        return String.format("%06d", otp); // Pads the number with leading zeros if necessary
    }

    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("BlogEcho - OTP Verification");
        message.setText("Your OTP for verification is: " + otp);
        javaMailSender.send(message);
    }

    private UserDto convertToDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getAge(),
                user.getCountry(),
                user.getEmail(),
                user.getGender(),
                user.getRole()
        );
    }
}