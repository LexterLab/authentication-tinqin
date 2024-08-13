package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.JWTException;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessToken;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenInput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenOutput;
import com.tinqinacademy.authentication.core.jwt.JWTTokenProvider;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static io.vavr.API.Match;


@Service
@Slf4j
public class ValidateAccessTokenProcessor extends BaseProcessor implements ValidateAccessToken {
    private final JWTTokenProvider tokenProvider;

    public ValidateAccessTokenProcessor(ConversionService conversionService, Validator validator,
                                        JWTTokenProvider tokenProvider) {
        super(conversionService, validator);
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Either<ErrorOutput, ValidateAccessTokenOutput> process(ValidateAccessTokenInput input) {
       return Try.of(() -> {
            log.info("Start validateAccessToken {}", input);
            boolean success = validateToken(input);
            ValidateAccessTokenOutput output = ValidateAccessTokenOutput
                    .builder()
                    .success(success)
                    .build();
            log.info("End validateAccessToken {}", output);
            return output;

        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        customCase(throwable, HttpStatus.BAD_REQUEST, JWTException.class),
                        defaultCase(throwable)
                ));
    }

    private boolean validateToken(ValidateAccessTokenInput input) {
        log.info("Start validateToken {}", input);
        return tokenProvider.validateToken(input.getAccessToken());
    }
}
