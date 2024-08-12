package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDontMatchException extends RuntimeException {
    private final String message;

    public PasswordDontMatchException() {
        this.message = Messages.PASSWORD_DONT_MATCH;
    }
}
