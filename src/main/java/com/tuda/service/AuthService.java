package com.tuda.service;

import com.tuda.dto.request.JwtRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> createAuthToken(JwtRequestDTO authRequest);
}
