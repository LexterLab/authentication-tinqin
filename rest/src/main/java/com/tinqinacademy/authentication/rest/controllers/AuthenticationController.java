package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePassword;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistration;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authentication.api.operations.demoteuser.DemoteUser;
import com.tinqinacademy.authentication.api.operations.demoteuser.DemoteUserInput;
import com.tinqinacademy.authentication.api.operations.demoteuser.DemoteUserOutput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUser;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserInput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserOutput;
import com.tinqinacademy.authentication.api.operations.login.Login;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import com.tinqinacademy.authentication.api.operations.promoteuser.PromoteUser;
import com.tinqinacademy.authentication.api.operations.promoteuser.PromoteUserInput;
import com.tinqinacademy.authentication.api.operations.promoteuser.PromoteUserOutput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPassword;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.authentication.api.operations.register.Register;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPassword;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPasswordInput;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPasswordOutput;
import com.tinqinacademy.authentication.api.operations.validaterecoverycode.ValidateRecoveryCode;
import com.tinqinacademy.authentication.api.operations.validaterecoverycode.ValidateRecoveryCodeInput;
import com.tinqinacademy.authentication.api.operations.validaterecoverycode.ValidateRecoveryCodeOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private final RecoverPassword recoverPassword;
    private final ResetPassword resetPassword;
    private final ValidateRecoveryCode validateRecoveryCode;
    private final ChangePassword changePassword;
    private final PromoteUser promoteUser;
    private final DemoteUser demoteUser;

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

    @Operation(
            summary = "Recover Password Rest API",
            description = "Recover Password Rest API is used for sending password recovery email to users"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
    })
    @PostMapping(RestAPIRoutes.RECOVER_PASSWORD)
    public ResponseEntity<?> recoverPassword(@RequestBody RecoverPasswordInput input) {
        Either<ErrorOutput, RecoverPasswordOutput> output = recoverPassword.process(input);
        return handleOutput(output, HttpStatus.OK);
    }

    @Operation(
            summary = "Reset Password Rest API",
            description = "Reset Password Rest API is used for resetting user's password with code"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @PostMapping(RestAPIRoutes.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordInput input) {
        Either<ErrorOutput, ResetPasswordOutput> output = resetPassword.process(input);
        return handleOutput(output, HttpStatus.OK);
    }

    @Operation(
            summary = "Validate Recovery Code Rest API",
            description = "Validate Recovery Code API is used for validating recovery code before resetting password"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @PostMapping(RestAPIRoutes.VALIDATE_RECOVERY_CODE)
    public ResponseEntity<?> validateRecoveryCode(@RequestBody ValidateRecoveryCodeInput input) {
        Either<ErrorOutput, ValidateRecoveryCodeOutput> output = validateRecoveryCode.process(input);
        return handleOutput(output, HttpStatus.OK);
    }

    @Operation(
            summary = "Change Password Rest API",
            description = "Change Password API is used for changing user's password"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping(RestAPIRoutes.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordInput input) {
        Either<ErrorOutput, ChangePasswordOutput> output = changePassword.process(input);
        return handleOutput(output, HttpStatus.OK);
    }

    @Operation(
            summary = "Promote User Rest API",
            description = "Promote User API is used for promoting users to admin"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping(RestAPIRoutes.PROMOTE_USER)
    public ResponseEntity<?> promoteUser(@RequestBody PromoteUserInput input) {
        Either<ErrorOutput, PromoteUserOutput> output = promoteUser.process(input);
        return handleOutput(output, HttpStatus.OK);
    }

    @Operation(
            summary = "Demote User Rest API",
            description = "Demote User API is used for demoting admins to users"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping(RestAPIRoutes.DEMOTE_USER)
    public ResponseEntity<?> demoteUser(@RequestBody DemoteUserInput input) {
        Either<ErrorOutput, DemoteUserOutput> output = demoteUser.process(input);
        return handleOutput(output, HttpStatus.OK);
    }


}
