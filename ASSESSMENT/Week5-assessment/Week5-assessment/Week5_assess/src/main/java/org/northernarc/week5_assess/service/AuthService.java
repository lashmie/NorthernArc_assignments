package org.northernarc.week5_assess.service;

import org.northernarc.week5_assess.dto.AuthRequest;
import org.northernarc.week5_assess.dto.AuthResponse;
import org.northernarc.week5_assess.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
}

