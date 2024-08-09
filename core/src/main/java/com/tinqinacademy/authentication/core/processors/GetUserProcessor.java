package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.operations.getuser.GetUser;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserInput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserOutput;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
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
public class GetUserProcessor extends BaseProcessor implements GetUser {
    private final UserRepository userRepository;

    public GetUserProcessor(ConversionService conversionService, Validator validator, UserRepository userRepository) {
        super(conversionService, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorOutput, GetUserOutput> process(GetUserInput input) {
        log.info("Start getUser {}", input);
        return Try.of(() -> {
            validateInput(input);
            User user = userRepository.findUserByUsernameIgnoreCase(input.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", input.getUsername()));
            GetUserOutput output = conversionService.convert(user, GetUserOutput.class);
            log.info("End getUser {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        validatorCase(throwable),
                        customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                        defaultCase(throwable)
                ));
    }
}
