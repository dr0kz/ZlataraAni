package com.webshop.repository;

import com.webshop.model.Role;
import com.webshop.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class UserRepository {
    private final PasswordEncoder passwordEncoder;
    private static User user;

    public UserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init(){
        user = new User("admin",passwordEncoder.encode("martin-zlatara-ani"), Role.ROLE_ADMIN);
    }
    public Optional<User> findByUsernameAndPassword(String username, String password){
        return user.getUsername().equals(username) && user.getPassword().equals(passwordEncoder.encode(password)) ? Optional.of(user)
                : Optional.empty();
    }
    public Optional<User> findByUsername(String username){
        return user.getUsername().equals(username) ? Optional.of(user)
                : Optional.empty();
    }
}
