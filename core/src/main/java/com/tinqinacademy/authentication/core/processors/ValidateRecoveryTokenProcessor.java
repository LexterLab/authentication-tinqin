package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.exceptions.TokenAlreadyConfirmedException;
import com.tinqinacademy.authentication.api.operations.validaterecoverycode.ValidateRecoveryCode;
import com.tinqinacademy.authentication.api.operations.validaterecoverycode.ValidateRecoveryCodeInput;
import com.tinqinacademy.authentication.api.operations.validaterecoverycode.ValidateRecoveryCodeOutput;
import com.tinqinacademy.authentication.persistence.crudrepositories.RecoveryTokenRepository;
import com.tinqinacademy.authentication.persistence.models.RecoveryToken;
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
public class ValidateRecoveryTokenProcessor extends BaseProcessor implements ValidateRecoveryCode {
    private final RecoveryTokenRepository recoveryTokenRepository;
    public ValidateRecoveryTokenProcessor(ConversionService conversionService, Validator validator,
                                          RecoveryTokenRepository recoveryTokenRepository) {
        super(conversionService, validator);
        this.recoveryTokenRepository = recoveryTokenRepository;
    }

    @Override
    public Either<ErrorOutput, ValidateRecoveryCodeOutput> process(ValidateRecoveryCodeInput input) {
        log.info("Start validateRecoveryCode {}", input);

       return Try.of(() -> {
           RecoveryToken token = fetchToken(input);

           checkIfTokenAlreadyUsed(token);

           ValidateRecoveryCodeOutput output = ValidateRecoveryCodeOutput.builder().build();

           log.info("End validateRecoveryCode {}", output);
           return output;
       }).toEither()
               .mapLeft(throwable -> Match(throwable).of(
                       customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                       customCase(throwable, HttpStatus.BAD_REQUEST, TokenAlreadyConfirmedException.class),
                       defaultCase(throwable)
               ));

    }

    private RecoveryToken fetchToken(ValidateRecoveryCodeInput input) {
        log.info("Start fetchToken {}", input);
        RecoveryToken token = recoveryTokenRepository.findById(input.getCode())
                .orElseThrow(() -> new ResourceNotFoundException("Recovery Token", "code", input.getCode()));
        log.info("End fetchToken {}", token);
        return token;
    }

    private void checkIfTokenAlreadyUsed(RecoveryToken token) {
        log.info("Start validateToken {}", token);

        if (token.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(token.getValue());
        }

        log.info("End validateToken {}", token);
    }
}
