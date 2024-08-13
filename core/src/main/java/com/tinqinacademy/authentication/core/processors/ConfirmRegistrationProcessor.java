package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.*;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistration;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authentication.persistence.crudrepositories.ConfirmationTokenRepository;
import com.tinqinacademy.authentication.persistence.models.ConfirmationToken;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class ConfirmRegistrationProcessor extends BaseProcessor implements ConfirmRegistration {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;


    public ConfirmRegistrationProcessor(ConversionService conversionService, Validator validator,
                                        ConfirmationTokenRepository confirmationTokenRepository,
                                        UserRepository userRepository) {
        super(conversionService, validator);
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Either<ErrorOutput, ConfirmRegistrationOutput> process(ConfirmRegistrationInput input) {
        log.info("Start confirmRegistration {}", input);
        return Try.of(() -> {
            ConfirmationToken token = validateToken(input);

            User user = fetchUserFromToken(token);
            checkIfUserAlreadyVerified(user);

            confirmUser(user);

            token.setConfirmedAt(LocalDateTime.now().toString());
            confirmationTokenRepository.save(token);

            ConfirmRegistrationOutput output = ConfirmRegistrationOutput.builder().build();
            log.info("End confirmRegistration {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, TokenAlreadyConfirmedException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, UserAlreadyVerifiedException.class),
                        defaultCase(throwable)
                ));
    }


    private ConfirmationToken validateToken(ConfirmRegistrationInput input) {
        log.info("Start validateToken {}", input);
        ConfirmationToken token = confirmationTokenRepository.findById(input.getConfirmationCode())
                .orElseThrow(() -> new ResourceNotFoundException("confirmationToken", "value", input.getConfirmationCode()));

        checkIfTokenAlreadyConfirmed(token);

        log.info("End validateToken {}", token);
        return token;
    }

    private void checkIfUserAlreadyVerified(User user) {
        log.info("Start checkIfTokenBelongsToUser {}", user.getUsername());

        if (user.getIsVerified()) {
            throw new UserAlreadyVerifiedException(user.getUsername());
        }
    }

    private void checkIfTokenAlreadyConfirmed(ConfirmationToken token) {
        log.info("Start checkIfTokenAlreadyConfirmed {}", token);

        if (token.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(token.getValue());
        }

        log.info("End checkIfTokenAlreadyConfirmed {}", token);
    }


    private User fetchUserFromToken(ConfirmationToken token) {
        log.info("Start fetchUserFromToken {}", token);
        User user = userRepository.findById(UUID.fromString(token.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", token.getUserId()));
        log.info("End fetchUserFromToken {}", user.getUsername());
        return user;
    }

    private void confirmUser(User user) {
        log.info("Start confirmUser {}", user.getUsername());
        userRepository.confirmEmail(user.getEmail());
        log.info("End confirmUser {}", user.getUsername());
    }
}
