package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.register.Register;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication REST APIs")
public class AuthenticationController extends BaseController {
    private final Register register;


    @PostMapping(RestAPIRoutes.AUTHENTICATE)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        Either<ErrorOutput, RegisterOutput> output = register.process(input);
        return handleOutput(output, HttpStatus.CREATED);
    }

}
