package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailNotConfirmedException extends RuntimeException {
    private final String message;
    private final String email;

    public EmailNotConfirmedException(String email) {
        this.email = email;
        this.message = String.format( Messages.EMAIL_NOT_VERIFIED, email);
    }
}
