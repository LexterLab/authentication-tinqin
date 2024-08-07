package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameAlreadyExistException  extends RuntimeException {
    private final String username;
    private final String message;

    public UsernameAlreadyExistException(String username) {
        this.username = username;
        this.message = String.format(Messages.USERNAME_ALREADY_EXISTS, username);
    }
}
