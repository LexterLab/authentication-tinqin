package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.EmailNotConfirmedException;
import com.tinqinacademy.authentication.api.operations.login.Login;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.login.LoginOutput;
import com.tinqinacademy.authentication.core.jwt.JWTTokenProvider;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static io.vavr.API.Match;

@Service
@Slf4j
public class LoginProcessor extends BaseProcessor implements Login {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider tokenProvider;

    public LoginProcessor(ConversionService conversionService, Validator validator,
                          AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
        super(conversionService, validator);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    @Override
    public Either<ErrorOutput, LoginOutput> process(LoginInput input) {
        log.info("Start login {}", input.getUsername());
        return Try.of(() -> {
            String accessToken = generateToken(input);

            setResponseHeaders(accessToken);

            log.info("End login {}", input.getUsername());
            return LoginOutput.builder().build();

        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        customCase(throwable, HttpStatus.FORBIDDEN, EmailNotConfirmedException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, AuthenticationException.class),
                        defaultCase(throwable)
                ));
    }

    private String generateToken(LoginInput input) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication.getName());
    }

    private void setResponseHeaders(String accessToken) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse();
            if (response != null) {
                response.setHeader("Authorization", "Bearer " + accessToken);
            }
        }
    }
}
