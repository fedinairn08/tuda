package com.tuda.controller;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.response.AccountingAppUserResponseDTO;
import com.tuda.service.AccountingUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountingAppUser")
@SecurityRequirement(name = "JWT")
public class AccountingAppUserController extends EntityController<AccountingAppUser> {

    private final AccountingUserService accountingUserService;

    private static final Class<AccountingAppUserResponseDTO> ACCOUNTING_APP_USER_DTO_CLASS =
            AccountingAppUserResponseDTO.class;

    public AccountingAppUserController(ModelMapper modelMapper, AccountingUserService accountingUserService) {
        super(modelMapper);
        this.accountingUserService = accountingUserService;
    }

    @PostMapping("/addAsParticipantForEvent")
    public ResponseEntity<ApiResponse<AccountingAppUserResponseDTO>> saveAsParticipantForEvent(
            @RequestParam Long eventId,
            String userLogin) {
        AccountingAppUser accountingAppUser =
                accountingUserService.createAccountingUserAsParticipant(eventId, userLogin);
        AccountingAppUserResponseDTO accountingAppUserResponseDTO =
                serialize(accountingAppUser, ACCOUNTING_APP_USER_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(accountingAppUserResponseDTO));
    }

    @PostMapping("/refuseToParticipate")
    public void delete(@RequestParam Long eventId, String userLogin) {
        accountingUserService.refuse(eventId, userLogin);
    }

    @PostMapping("/addAsVolunteerForEvent")
    public ResponseEntity<ApiResponse<AccountingAppUserResponseDTO>> saveAsVolunteerForEvent(
            @RequestParam Long eventId,
            String userLogin) {
        AccountingAppUser accountingAppUser =
                accountingUserService.createAccountingUserAsVolunteer(eventId, userLogin);
        AccountingAppUserResponseDTO accountingAppUserResponseDTO =
                serialize(accountingAppUser, ACCOUNTING_APP_USER_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(accountingAppUserResponseDTO));
    }

    @PostMapping("/markPresence")
    public ResponseEntity<ApiResponse<AccountingAppUserResponseDTO>> markPresence(
            @RequestParam Long eventId,
            String userLogin) {
        AccountingAppUser accountingAppUser =
                accountingUserService.markPresence(eventId, userLogin);
        AccountingAppUserResponseDTO accountingAppUserResponseDTO =
                serialize(accountingAppUser, ACCOUNTING_APP_USER_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(accountingAppUserResponseDTO));
    }


}
