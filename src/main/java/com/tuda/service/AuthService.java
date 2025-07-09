package com.tuda.service;

import com.tuda.dto.request.JwtRefreshRequestDTO;
import com.tuda.dto.request.JwtSignInRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.response.JwtResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

public interface AuthService {
    JwtResponseDTO signIn(JwtSignInRequestDTO authRequest);
    JwtResponseDTO signUp(JwtSignUpRequestDTO register);
    JwtResponseDTO refresh(JwtRefreshRequestDTO refreshToken);
}
