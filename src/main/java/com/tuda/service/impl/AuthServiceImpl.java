package com.tuda.service.impl;

import com.tuda.config.JwtTokenUtils;
import com.tuda.data.entity.AppUser;
import com.tuda.dto.request.JwtLoginRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.response.JwtResponseDTO;
import com.tuda.repository.UserRepository;
import com.tuda.service.AuthService;
import com.tuda.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> createAuthToken(JwtLoginRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Неправильный логин или пароль " + authRequest.getPassword());
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getLogin());
        Long userId = userRepository.findByLogin(userDetails.getUsername()).get().getId();

        String token = jwtTokenUtils.generateToken(userDetails, userId);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @Override
    public ResponseEntity<?> register(JwtSignUpRequestDTO registerRequest) {
        if (userRepository.findByLogin(registerRequest.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Пользователь с таким логином уже существует");
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        AppUser newUser = modelMapper.map(registerRequest, AppUser.class);
        AppUser savedUser = userRepository.save(newUser);

        UserDetails userDetails = userService.loadUserByUsername(savedUser.getLogin());
        String token = jwtTokenUtils.generateToken(userDetails, savedUser.getId());

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}