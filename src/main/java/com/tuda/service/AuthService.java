package com.tuda.service;

import com.tuda.dto.request.JwtLoginRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> createAuthToken(JwtLoginRequestDTO authRequest);
    ResponseEntity<?> register(JwtSignUpRequestDTO register);
}
