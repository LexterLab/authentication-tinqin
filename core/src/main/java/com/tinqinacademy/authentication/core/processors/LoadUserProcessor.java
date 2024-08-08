package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.EmailNotConfirmedException;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetails;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetailsInput;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetailsOutput;
import com.tinqinacademy.authentication.core.security.CustomUserDetailsService;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static io.vavr.API.Match;

@Service
@Slf4j
public class LoadUserProcessor extends BaseProcessor implements LoadUserDetails {
    private final CustomUserDetailsService customUserDetailsService;

    public LoadUserProcessor(ConversionService conversionService, Validator validator, CustomUserDetailsService customUserDetailsService) {
        super(conversionService, validator);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Either<ErrorOutput, LoadUserDetailsOutput> process(LoadUserDetailsInput input) {
        log.info("Start loadUser {}", input);
        return Try.of(() -> {
            User userDetails = (User) customUserDetailsService.loadUserByUsername(input.getUsername());
            LoadUserDetailsOutput output = LoadUserDetailsOutput
                    .builder()
                    .userDetails(userDetails)
                    .build();
            log.info("End loadUser {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        customCase(throwable, HttpStatus.FORBIDDEN, EmailNotConfirmedException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, AuthenticationException.class),
                        defaultCase(throwable)
                ));
    }
}
