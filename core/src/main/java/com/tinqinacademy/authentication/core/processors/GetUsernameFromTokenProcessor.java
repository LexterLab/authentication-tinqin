package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromToken;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenInput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenOutput;
import com.tinqinacademy.authentication.core.jwt.JWTTokenProvider;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import static io.vavr.API.Match;

@Service
@Slf4j
public class GetUsernameFromTokenProcessor extends BaseProcessor implements GetUsernameFromToken {
    private final JWTTokenProvider tokenProvider;

    public GetUsernameFromTokenProcessor(ConversionService conversionService, Validator validator,
                                         JWTTokenProvider tokenProvider) {
        super(conversionService, validator);
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Either<ErrorOutput, GetUsernameFromTokenOutput> process(GetUsernameFromTokenInput input) {
        log.info("Start getUsernameFromToken {}", input);
       return Try.of(() -> {
            String username = tokenProvider.getUsername(input.getToken());
            GetUsernameFromTokenOutput output = GetUsernameFromTokenOutput.builder()
                    .username(username)
                    .build();
            log.info("End getUsernameFromToken {}", output);
            return output;
        }).toEither()
               .mapLeft(throwable -> Match(throwable).of(
                       defaultCase(throwable)
               ));
    }
}
