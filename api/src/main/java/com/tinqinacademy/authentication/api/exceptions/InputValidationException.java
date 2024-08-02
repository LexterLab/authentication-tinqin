package com.tinqinacademy.authentication.api.exceptions;

import com.tinqinacademy.authentication.api.errors.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class InputValidationException extends RuntimeException {
    private List<Error> errors;
}
