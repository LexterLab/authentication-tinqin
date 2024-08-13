package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.operations.finduser.FindUser;
import com.tinqinacademy.authentication.api.operations.finduser.FindUserInput;
import com.tinqinacademy.authentication.api.operations.finduser.FindUserOutput;
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
public class FindUserProcessor extends BaseProcessor implements FindUser {
    private final UserRepository userRepository;
    public FindUserProcessor(ConversionService conversionService, Validator validator, UserRepository userRepository) {
        super(conversionService, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<ErrorOutput, FindUserOutput> process(FindUserInput input) {
        log.info("Start findUser {}", input);

        return Try.of(() -> {
            validateInput(input);
            User user = fetchUserFromInput(input);

            FindUserOutput output = FindUserOutput.builder()
                    .userId(user.getId())
                    .build();

            log.info("End findUser {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        validatorCase(throwable),
                        customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                        defaultCase(throwable)
                ));
    }


    private User fetchUserFromInput(FindUserInput input) {
        log.info("Start fetchUserFromInput {}", input);
        User user = userRepository.findUserByPhoneNo(input.getPhoneNo())
                .orElseThrow(() -> new ResourceNotFoundException("User", "phoneNo", input.getPhoneNo()));
        log.info("End fetchUserFromInput {}", user.getUsername());
        return user;
    }
}
