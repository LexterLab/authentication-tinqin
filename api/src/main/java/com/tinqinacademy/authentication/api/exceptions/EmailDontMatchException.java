package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDontMatchException extends RuntimeException {
    private final String email;
    private final String inputEmail;
    private String message;

    public EmailDontMatchException(String email, String inputEmail) {
        this.email = email;
        this.inputEmail = inputEmail;
        this.message = String.format(Messages.EMAIL_DONT_MATCH, inputEmail, email);

    }
}
