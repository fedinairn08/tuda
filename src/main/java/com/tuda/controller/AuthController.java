package com.tuda.controller;

import com.tuda.dto.request.JwtLoginRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody JwtLoginRequestDTO authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody JwtSignUpRequestDTO register) {
        return authService.register(register);
    }

}
