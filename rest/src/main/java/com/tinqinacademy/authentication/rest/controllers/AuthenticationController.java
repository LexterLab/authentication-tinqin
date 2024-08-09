package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistration;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUser;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserInput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserOutput;
import com.tinqinacademy.authentication.api.operations.login.Login;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import com.tinqinacademy.authentication.api.operations.register.Register;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication REST APIs")
public class AuthenticationController extends BaseController {
    private final Register register;
    private final Login login;
    private final ConfirmRegistration confirmRegistration;
    private final GetUser getUser;

    @Operation(
            summary = "Register Rest API",
            description = "Register Rest API is used for registering user and sending confirmation email"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "HTTP STATUS 201 CREATED"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
    })
    @PostMapping(RestAPIRoutes.REGISTER)
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        Either<ErrorOutput, RegisterOutput> output = register.process(input);
        return handleOutput(output, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login Rest API",
            description = "Login Rest API is used for signing in user and returning access token header"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
    })
    @PostMapping(RestAPIRoutes.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        Either<ErrorOutput, LoginOutput> output = login.process(input);
        return handleOutput(output, HttpStatus.OK);
    }


    @Operation(
            summary = "Confirm Registration Rest API",
            description = "Confirm Registration Rest API is used for verifying users"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
    })
    @PostMapping(RestAPIRoutes.CONFIRM_REGISTRATION)
    public ResponseEntity<?> confirmRegistration(@RequestBody ConfirmRegistrationInput input) {
        Either<ErrorOutput, ConfirmRegistrationOutput> output = confirmRegistration.process(input);
        return handleOutput(output, HttpStatus.OK);
    }

    @Operation(
            summary = "Get User Info Rest API",
            description = "Get User Info  Rest API is used for retrieving signed  user's info"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @GetMapping(RestAPIRoutes.GET_USER)
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        Either<ErrorOutput, GetUserOutput> outputs = getUser.process(GetUserInput
                .builder()
                .username(username)
                .build());
        return handleOutput(outputs, HttpStatus.OK);
    }

}
