package com.giannis.core_api.services;

import com.giannis.core_api.dto.AuthRequest;
import com.giannis.core_api.dto.AuthResponse;
import com.giannis.core_api.dto.UserRequestDto;

public interface AuthService {
    
    AuthResponse register(UserRequestDto request);
    
    AuthResponse authenticate(AuthRequest request);
    
}