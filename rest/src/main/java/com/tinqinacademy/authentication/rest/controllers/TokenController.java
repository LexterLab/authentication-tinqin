package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.errors.ErrorOutput;
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
}
