package com.tuda.controller;

import com.tuda.dto.request.JwtSignInRequestDTO;
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
    public ResponseEntity<?> signIn(@RequestBody JwtSignInRequestDTO authRequest) {
        return authService.signIn(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody JwtSignUpRequestDTO register) {
        return authService.signUp(register);
    }

}
