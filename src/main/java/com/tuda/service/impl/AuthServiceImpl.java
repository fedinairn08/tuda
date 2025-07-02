package com.tuda.service.impl;

import com.tuda.config.JwtTokenUtils;
import com.tuda.converter.AppUserConverter;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Organization;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.dto.request.JwtLoginRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.response.JwtResponseDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.OrganizationRepository;
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
    private final OrganizationRepository organizationRepository;
    private final AppUserConverter appUserConverter;

    public AuthServiceImpl(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserRepository userRepository, ModelMapper modelMapper, OrganizationRepository organizationRepository, AppUserConverter appUserConverter) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.organizationRepository = organizationRepository;
        this.appUserConverter = appUserConverter;
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

        AppUser newUser = new AppUser();
        newUser.setLogin(registerRequest.getLogin());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setName(registerRequest.getName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setPatronymic(registerRequest.getPatronymic());
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());

        if (registerRequest.getOrganizationName() != null) {
            if (organizationRepository.findByName(registerRequest.getOrganizationName()).isPresent()) {
                throw new NotFoundException("Организация с таким названием уже существует");
            }

            Organization newOrganization = new Organization();
            newOrganization.setName(registerRequest.getOrganizationName());
            newOrganization.setPhoneNumber(registerRequest.getOrganizationPhoneNumber());

            Organization savedOrganization = organizationRepository.save(newOrganization);
            newUser.setOrganization(savedOrganization);
        }

        AppUser savedUser = userRepository.save(newUser);

        UserDetails userDetails = userService.loadUserByUsername(savedUser.getLogin());
        String token = jwtTokenUtils.generateToken(userDetails, savedUser.getId());

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

}