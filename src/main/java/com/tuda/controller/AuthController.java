package com.tuda.controller;

import com.tuda.dto.request.JwtSignInRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.request.JwtRefreshRequestDTO;
import com.tuda.dto.response.JwtResponseDTO;
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
    public ResponseEntity<JwtResponseDTO> signIn(@RequestBody JwtSignInRequestDTO authRequest) {
        return ResponseEntity.ok(authService.signIn(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> signUp(@RequestBody JwtSignUpRequestDTO register) {
        return  ResponseEntity.ok(authService.signUp(register));
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponseDTO> getNewRefreshToken(@RequestBody JwtRefreshRequestDTO request) {
        return  ResponseEntity.ok(authService.refresh(request));
    }
}
