package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;

@Getter
public class PhoneNoExistsException  extends RuntimeException {
    private final String phone;
    private final String message;

    public PhoneNoExistsException(String phone) {
        this.phone = phone;
        this.message = String.format(Messages.PHONE_NO_ALREADY_EXISTS, phone);
    }
}
