package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;

@Getter
public class MaxPermissionsException extends RuntimeException {
    private final String username;
    private final String message;

    public MaxPermissionsException(String username) {
        this.username = username;
        this.message = String.format(Messages.ALREADY_ADMIN, username);
    }
}
