package com.tuda.service;

import com.tuda.dto.request.JwtSignInRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> signIn(JwtSignInRequestDTO authRequest);
    ResponseEntity<?> signUp(JwtSignUpRequestDTO register);
}
