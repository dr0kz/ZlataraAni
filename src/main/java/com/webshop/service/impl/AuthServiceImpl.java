package com.webshop.service.impl;

import com.webshop.model.User;
import com.webshop.model.exceptions.InvalidArgumentsException;
import com.webshop.model.exceptions.InvalidUserCredentialsException;
import com.webshop.repository.UserRepository;
import com.webshop.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if(username==null || username.isEmpty() || password==null || password.isEmpty())
            throw new InvalidArgumentsException();
        return this.userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidUserCredentialsException::new);
    }
}
