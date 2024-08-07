package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenExpiredException  extends RuntimeException {
    private final String message;
    private final String token;

    public TokenExpiredException(String token) {
        this.token = token;
        this.message = String.format(Messages.TOKEN_EXPIRED, token);
    }
}
