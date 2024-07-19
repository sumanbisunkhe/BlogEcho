package com.BlogEcho.BlogEcho.service.impl;

import com.BlogEcho.BlogEcho.model.User;
import com.BlogEcho.BlogEcho.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomCustomerDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public CustomCustomerDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name()) // Convert enum Role to string
                .accountLocked(!user.isEnabled()) // Account locked if not enabled
                .build();
    }
}
