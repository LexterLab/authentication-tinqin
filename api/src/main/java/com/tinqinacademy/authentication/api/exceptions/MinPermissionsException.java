package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;

@Getter
public class MinPermissionsException extends RuntimeException {
    private final String username;
    private final String message;

    public MinPermissionsException(String username) {
        this.username = username;
        this.message = String.format(Messages.MIN_PERMISSIONS, username);
    }
}
