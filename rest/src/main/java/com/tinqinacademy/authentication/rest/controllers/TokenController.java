package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromToken;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenInput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenOutput;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetails;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetailsInput;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetailsOutput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessToken;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenInput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Token REST APIs")
public class TokenController extends BaseController {
    private final ValidateAccessToken validateAccessToken;
    private final GetUsernameFromToken getUsernameFromToken;
    private final LoadUserDetails loadUserDetails;


    @Operation(
            summary = "Validate Access Token Rest API",
            description = "Validate Access Token Rest API is used for verifying access tokens"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
    })
    @PostMapping(RestAPIRoutes.VALIDATE_TOKEN)
    public ResponseEntity<?> validateAccessToken(@RequestBody ValidateAccessTokenInput input) {
        Either<ErrorOutput, ValidateAccessTokenOutput> results = validateAccessToken.process(input);
        return handleOutput(results, HttpStatus.OK);
    }

    @Operation(
            summary = "Get Username from Token Rest API",
            description = "Get Username from Token Rest API is used for verifying access tokens"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
    })
    @PostMapping(RestAPIRoutes.GET_USER_FROM_TOKEN)
    public ResponseEntity<?> getUsernameFromToken(@RequestBody GetUsernameFromTokenInput input) {
        Either<ErrorOutput, GetUsernameFromTokenOutput> results = getUsernameFromToken
                .process(input);
        return handleOutput(results, HttpStatus.OK);
    }


    @Operation(
            summary = "Load User from Token Rest API",
            description = "Get Username from Token Rest API is used for verifying access tokens"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "HTTP STATUS 200 OK"),
            @ApiResponse(responseCode = "400", description = "HTTP STATUS 400 BAD REQUEST"),
    })
    @PostMapping(RestAPIRoutes.LOAD_USER)
    public ResponseEntity<?> loadUser(@RequestBody LoadUserDetailsInput input) {
        Either<ErrorOutput, LoadUserDetailsOutput> result = loadUserDetails.process(input);
        return handleOutput(result, HttpStatus.OK);
    }
}
