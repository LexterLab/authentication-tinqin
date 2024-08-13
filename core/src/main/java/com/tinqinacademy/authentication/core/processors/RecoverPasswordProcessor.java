package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPassword;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.authentication.persistence.crudrepositories.RecoveryTokenRepository;
import com.tinqinacademy.authentication.persistence.models.RecoveryToken;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import com.tinqinacademy.emails.api.operations.sendrecoveryemail.SendRecoveryEmailInput;
import com.tinqinacademy.emails.restexport.EmailClient;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class RecoverPasswordProcessor extends BaseProcessor implements RecoverPassword {
    private final UserRepository userRepository;
    private final EmailClient emailClient;
    private final RecoveryTokenRepository recoveryTokenRepository;
    public RecoverPasswordProcessor(ConversionService conversionService, Validator validator,
                                    UserRepository userRepository, EmailClient emailClient,
                                    RecoveryTokenRepository recoveryTokenRepository) {
        super(conversionService, validator);
        this.userRepository = userRepository;
        this.emailClient = emailClient;
        this.recoveryTokenRepository = recoveryTokenRepository;
    }

    @Override
    @Transactional
    @Async
    public Either<ErrorOutput, RecoverPasswordOutput> process(RecoverPasswordInput input) {
        log.info("Start recoverPassword {}", input);
        return Try.of(() -> {
            Optional<User> user  = userRepository.findUserByEmailIgnoreCase(input.getEmail());
            if (user.isPresent()) {
                String code = createRecoveryToken(user.get());

                SendRecoveryEmailInput emailInput = SendRecoveryEmailInput.builder()
                        .recipient(user.get().getEmail())
                        .code(code)
                        .build();
                emailClient.sendRecoveryEmail(emailInput);
            }
            RecoverPasswordOutput output = RecoverPasswordOutput.builder().build();
            log.info("RecoverPassword email sent to {}", output);
            return output;

        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        feignCase(throwable),
                        defaultCase(throwable)
                ));
    }

    private String createRecoveryToken(User user) {
        log.info("Start createRecoveryToken {}", user.getUsername());
        String token = UUID.randomUUID().toString();
        RecoveryToken recoveryToken = RecoveryToken.builder()
                .createdAt(LocalDateTime.now().toString())
                .value(token)
                .expirySeconds(900L)
                .userId(user.getId().toString())
                .build();
        recoveryTokenRepository.save(recoveryToken);
        log.info("End createConfirmationToken {}", recoveryToken);
        return token;
    }
}
