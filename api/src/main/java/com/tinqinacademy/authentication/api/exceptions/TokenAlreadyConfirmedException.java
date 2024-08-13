package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAlreadyConfirmedException extends RuntimeException {
    private final String token;
    private final String message;

    public TokenAlreadyConfirmedException(String token) {
        this.token = token;
        this.message = String.format(Messages.TOKEN_ALREADY_CONFIRMED, token);
    }
}
