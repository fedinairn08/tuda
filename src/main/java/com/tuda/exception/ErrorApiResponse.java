package com.tuda.exception;

import lombok.Data;

@Data
public class ErrorApiResponse {
    private boolean error;
    private String errorMessage;

    public ErrorApiResponse(String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
    }
}