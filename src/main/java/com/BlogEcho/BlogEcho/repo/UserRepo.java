package com.BlogEcho.BlogEcho.repo;

import com.BlogEcho.BlogEcho.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);

    boolean existsByEmail(String mail);

}
