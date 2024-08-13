package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException  extends RuntimeException {
    private final String message;
    private final String username;

    public UnauthorizedAccessException(String username) {
        this.username = username;
        this.message = String.format(Messages.UNAUTHORIZED_USER, username);

    }
}
