package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.exceptions.TokenAlreadyConfirmedException;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPassword;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPasswordInput;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPasswordOutput;
import com.tinqinacademy.authentication.persistence.crudrepositories.RecoveryTokenRepository;
import com.tinqinacademy.authentication.persistence.models.RecoveryToken;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class ResetPasswordProcessor extends BaseProcessor implements ResetPassword {
    private final UserRepository userRepository;
    private final RecoveryTokenRepository recoveryTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordProcessor(ConversionService conversionService, Validator validator,
                                  UserRepository userRepository, RecoveryTokenRepository recoveryTokenRepository,
                                  PasswordEncoder passwordEncoder) {
        super(conversionService, validator);
        this.userRepository = userRepository;
        this.recoveryTokenRepository = recoveryTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Either<ErrorOutput, ResetPasswordOutput> process(ResetPasswordInput input) {
        log.info("Start resetPassword {}", input.getCode());

        return Try.of(() -> {
            validateInput(input);

            RecoveryToken token = fetchToken(input);

            checkIfTokenAlreadyUsed(token);

            token.setConfirmedAt(LocalDateTime.now().toString());
            recoveryTokenRepository.save(token);

            User user = fetchUserFromToken(token);

            user.setPassword(passwordEncoder.encode(input.getPassword()));

            ResetPasswordOutput output = ResetPasswordOutput.builder().build();

            log.info("End resetPassword {}", output);

            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        validatorCase(throwable),
                        customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, TokenAlreadyConfirmedException.class),
                        defaultCase(throwable)
                ));
    }


    private RecoveryToken fetchToken(ResetPasswordInput input) {
        log.info("Start fetchToken {}", input.getCode());
        RecoveryToken token = recoveryTokenRepository.findById(input.getCode())
                .orElseThrow(() -> new ResourceNotFoundException("Recovery Token", "code", input.getCode()));
        log.info("End fetchToken {}", token);
        return token;
    }

    private User fetchUserFromToken(RecoveryToken token) {
        log.info("Start fetchUserFromToken {}", token);
        User user = userRepository.findById(UUID.fromString(token.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", token.getUserId()));
        log.info("End fetchUserFromToken {}", user.getUsername());
        return user;
    }

    private void checkIfTokenAlreadyUsed(RecoveryToken token) {
        log.info("Start checkIfTokenAlreadyUsed {}", token);

        if (token.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(token.getValue());
        }

        log.info("End checkIfTokenAlreadyUsed {}", token);
    }
}
