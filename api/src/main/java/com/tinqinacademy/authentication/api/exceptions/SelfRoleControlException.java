package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.Messages;
import lombok.Getter;

@Getter
public class SelfRoleControlException extends RuntimeException {
    private final String message;

    public SelfRoleControlException() {
        this.message = Messages.SELF_ROLE_MODIFICATION;
    }
}
