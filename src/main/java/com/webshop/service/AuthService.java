package com.webshop.service;

import com.webshop.model.User;

public interface AuthService {
    User login(String username, String password);
}

