package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.operations.logout.Logout;
import com.tinqinacademy.authentication.api.operations.logout.LogoutInput;
import com.tinqinacademy.authentication.api.operations.logout.LogoutOutput;
import com.tinqinacademy.authentication.core.jwt.JWTTokenProvider;
import com.tinqinacademy.authentication.persistence.crudrepositories.ExpiredJWTRepository;
import com.tinqinacademy.authentication.persistence.models.ExpiredJWT;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static io.vavr.API.Match;

@Service
@Slf4j
public class LogoutProcessor extends BaseProcessor implements Logout {
    private final UserRepository userRepository;
    private final ExpiredJWTRepository expiredJWTRepository;
    private final HttpServletRequest request;
    private final JWTTokenProvider tokenProvider;

    public LogoutProcessor(ConversionService conversionService, Validator validator, UserRepository userRepository,
                           ExpiredJWTRepository expiredJWTRepository, HttpServletRequest request,
                           JWTTokenProvider tokenProvider) {
        super(conversionService, validator);
        this.userRepository = userRepository;
        this.expiredJWTRepository = expiredJWTRepository;
        this.request = request;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Either<ErrorOutput, LogoutOutput> process(LogoutInput input) {
        log.info("Start logout {}", input);

        return Try.of(() -> {
            fetchCurrentUser();
            expiredJWTRepository.save(buildExpiredJWT(fetchCurrentToken()));

            LogoutOutput output = LogoutOutput.builder().build();
            log.info("End logout {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                        defaultCase(throwable)
                ));
    }



    private ExpiredJWT buildExpiredJWT(String token) {
        log.info("Start buildExpiredJWT {}", token);
        ExpiredJWT expiredJWT = ExpiredJWT.builder()
                .value(token)
                .expirySeconds(tokenProvider.getSecondsDifferenceFromExpiration(token))
                .build();
        log.info("End buildExpiredJWT {}", expiredJWT);
        return expiredJWT;
    }

    private void fetchCurrentUser() {
        String username = (String) request.getAttribute("username");
        log.info("Start fetchUser {}", username);
        User user = userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        log.info("End fetchUser {}", user.getUsername());
    }

    private String fetchCurrentToken() {
        log.info("Start fetchCurrentToken");
        String token = (String) request.getAttribute("token");
        log.info("End fetchCurrentToken {}", token);
        return token;
    }
}
