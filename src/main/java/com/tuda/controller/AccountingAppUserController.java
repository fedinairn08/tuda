package com.tuda.controller;

import com.tuda.dto.ApiResponse;
import com.tuda.dto.response.AccountingAppUserResponseDTO;
import com.tuda.entity.AccountingAppUser;
import com.tuda.service.AccountingUserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accountingAppUser")
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
    public void delete(@RequestParam Long id) {
        accountingUserService.refuseToParticipate(id);
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
}
