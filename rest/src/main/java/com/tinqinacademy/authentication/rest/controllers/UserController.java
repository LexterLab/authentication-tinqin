package com.tinqinacademy.authentication.rest.controllers;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authentication.api.operations.finduser.FindUser;
import com.tinqinacademy.authentication.api.operations.finduser.FindUserInput;
import com.tinqinacademy.authentication.api.operations.finduser.FindUserOutput;
import com.tinqinacademy.restexportprocessor.main.RestExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User REST APIs")
@RestController
public class UserController extends BaseController {
    private final FindUser findUser;

    public UserController(FindUser findUser) {
        this.findUser = findUser;
    }

    @Operation(
            summary = "Find User Rest API",
            description = "Find User Rest API is used for finding user by phoneNo"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP STATUS 200 SUCCESS", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FindUserOutput.class))
            ),
            @ApiResponse(responseCode = "404", description = "HTTP STATUS 404 NOT FOUND")
    })
    @RestExport
    @PostMapping(RestAPIRoutes.FIND_USER)
    public ResponseEntity<?> findUser(@RequestBody FindUserInput findUserInput) {
        Either<ErrorOutput, FindUserOutput> output = findUser.process(findUserInput);
        return handleOutput(output, HttpStatus.OK);
    }
}
