package com.tuda.service.impl;

import com.tuda.config.JwtTokenUtils;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Organization;
import com.tuda.dto.request.JwtSignInRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.request.OrganizationRequestDTO;
import com.tuda.dto.response.JwtResponseDTO;
import com.tuda.exception.BadRequestException;
import com.tuda.exception.NotFoundException;
import com.tuda.service.AuthService;
import com.tuda.service.OrganizationService;
import com.tuda.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final OrganizationService organizationService;

    public AuthServiceImpl(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, OrganizationService organizationService) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.organizationService = organizationService;
    }

    public ResponseEntity<?> signIn(JwtSignInRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new NotFoundException(String.format("Пользователя с таким логином %s и паролем %s не существует", authRequest.getLogin(), authRequest.getPassword()));
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getLogin());
        Long userId = userService.getByLogin(userDetails.getUsername()).get().getId();

        String token = jwtTokenUtils.generateToken(userDetails, userId);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @Override
    public ResponseEntity<?> signUp(JwtSignUpRequestDTO registerRequest) {
        if (userService.getByLogin(registerRequest.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Пользователь с таким логином уже существует");
        }

        AppUser appUser = appUserCreating(registerRequest);
        organizationCreating(registerRequest, appUser);

        AppUser savedUser = userService.create(appUser);
        UserDetails userDetails = userService.loadUserByUsername(savedUser.getLogin());

        String token = jwtTokenUtils.generateToken(userDetails, savedUser.getId());
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    private AppUser appUserCreating(JwtSignUpRequestDTO registerRequest) {
        return AppUser.builder()
                .login(registerRequest.getLogin())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .lastName(registerRequest.getLastName())
                .patronymic(registerRequest.getPatronymic())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }

    private void organizationCreating(JwtSignUpRequestDTO registerRequest, AppUser appUser) {
        if (registerRequest.getOrganizationName() != null) {
            if (organizationService.getByName(registerRequest.getOrganizationName()).isPresent()) {
                throw new BadRequestException("Организация с таким названием уже существует");
            }

            OrganizationRequestDTO newOrganization = new OrganizationRequestDTO();
            newOrganization.setName(registerRequest.getOrganizationName());
            newOrganization.setPhoneNumber(registerRequest.getOrganizationPhoneNumber());

            Organization savedOrganization = organizationService.addOrganization(newOrganization);
            appUser.setOrganization(savedOrganization);
        }
    }

}