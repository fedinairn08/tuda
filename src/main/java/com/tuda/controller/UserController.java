package com.tuda.controller;

import com.tuda.data.entity.AppUser;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.dto.response.AppUserResponseDTO;
import com.tuda.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends EntityController<AppUser> {

    private final UserService userService;

    private static final Class<AppUserResponseDTO> APP_USER_DTO_CLASS = AppUserResponseDTO.class;

    public UserController(ModelMapper modelMapper, UserService userService) {
        super(modelMapper);
        this.userService = userService;
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<AppUserResponseDTO>> getUser(@PathVariable long id) {
        AppUser user = userService.getById(id);
        AppUserResponseDTO appUserResponseDTO = serialize(user, APP_USER_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(appUserResponseDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<AppUserResponseDTO>> changeUserRefs(@RequestBody AppUserRequestDTO request, String login) {
        AppUser user = userService.updateUser(request, login);
        AppUserResponseDTO appUserResponseDTO = serialize(user, APP_USER_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(appUserResponseDTO));
    }
}
