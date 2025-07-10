package com.tuda.service.impl;

import com.tuda.repository.AccountingUserRepository;
import com.tuda.repository.GuestRepository;
import com.tuda.service.KeyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class KeyServiceImpl implements KeyService {
    private final AccountingUserRepository accountingUserRepository;
    private final GuestRepository guestRepository;

    @Override
    public Optional<?> findEntityByKey(String key) {
        return guestRepository.findByKeyId(key)
                .<Optional<?>>map(Optional::of)
                .orElseGet(() -> accountingUserRepository.findByKeyId(key));
    }

    @Override
    public String generateKey() {
        return UUID.randomUUID().toString();
    }
}
