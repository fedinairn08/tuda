package com.tuda.service;

import java.util.Optional;

public interface KeyService {
    Optional<?> findEntityByKey(String key);
    String generateKey();
}
