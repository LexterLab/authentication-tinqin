package com.tinqinacademy.authentication.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class JWTException  extends RuntimeException {
    private String message;
    private HttpStatus status;
}
