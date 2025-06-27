package com.tuda.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean error;
    private String errorMassage;
    private T result;

    public ApiResponse(boolean error, T result) {
        this.error = error;
        this.result = result;
    }

    public ApiResponse(T result) {
        this.error = false;
        this.result = result;
    }
}
