package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyVerifiedException extends RuntimeException {
    private final String username;
    private final String message;

    public UserAlreadyVerifiedException(String username) {
        this.username = username;
        this.message = String.format(Messages.USER_ALREADY_VERIFIED, username);
    }
}
