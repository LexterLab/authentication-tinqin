package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class MinNumberOfAdminsException extends RuntimeException {
    private final String message;

    public MinNumberOfAdminsException(String message) {
        this.message = Messages.MIN_ADMIN_AMOUNT;
    }
}
