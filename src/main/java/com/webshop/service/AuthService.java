package com.webshop.service;

import com.webshop.model.User;
import com.webshop.model.exceptions.InvalidArgumentsException;
import com.webshop.model.exceptions.InvalidUserCredentialsException;
import com.webshop.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        if(username==null || username.isEmpty() || password==null || password.isEmpty())
            throw new InvalidArgumentsException();
        return this.userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidUserCredentialsException::new);
    }
}
