package com.BlogEcho.BlogEcho.dto;

import com.BlogEcho.BlogEcho.enums.Gender;
import com.BlogEcho.BlogEcho.enums.Role;
import com.BlogEcho.BlogEcho.model.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    private Integer age;

    @NotBlank(message = "Country is required")
    private String country;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Role is required")
    private Role role;

    private String otpCode;
    private boolean enabled;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // Constructors
    public UserDto() {
    }

    public UserDto(Long id, String username, Integer age, String country, String email, Gender gender, Role role,boolean enabled) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.country = country;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.enabled = enabled;
        this.otpCode = null; // OTP will be generated automatically in User class
    }
    public UserDto(Long id, String username, Integer age, String country, String email, Gender gender, Role role) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.country = country;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.otpCode = null; // OTP will be generated automatically in User class
    }


    public UserDto(Long id, String username, Integer age, String country, String email, String password, Gender gender, Role role, String otpCode, boolean enabled) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.country = country;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.otpCode = otpCode;
        this.enabled = enabled;
    }

    public UserDto(String username, Integer age, String country, String email, String password, Gender gender, Role role, String otp) {
        this.username = username;
        this.age = age;
        this.country = country;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.otpCode = otp;
    }
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
