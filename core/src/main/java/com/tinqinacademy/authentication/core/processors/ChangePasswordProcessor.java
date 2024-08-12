package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.*;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePassword;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authentication.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static io.vavr.API.Match;

@Slf4j
@Service
public class ChangePasswordProcessor extends BaseProcessor implements ChangePassword {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

    public ChangePasswordProcessor(ConversionService conversionService, Validator validator,
                                   PasswordEncoder passwordEncoder, UserRepository userRepository,
                                   HttpServletRequest request) {
        super(conversionService, validator);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.request = request;
    }

    @Override
    public Either<ErrorOutput, ChangePasswordOutput> process(ChangePasswordInput input) {
        log.info("Start changePassword {}", input.getEmail());
         return Try.of(() -> {
            User user = fetchUser();

            validateProcess(input, user);

            user.setPassword(passwordEncoder.encode(input.getNewPassword()));
            userRepository.save(user);

            ChangePasswordOutput output = ChangePasswordOutput.builder().build();

            log.info("End changePassword {}", output);

            return output;
        }).toEither()
                 .mapLeft(throwable -> Match(throwable).of(
                         validatorCase(throwable),
                         customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                         customCase(throwable, HttpStatus.BAD_REQUEST, EmailDontMatchException.class),
                         customCase(throwable, HttpStatus.BAD_REQUEST, PasswordDontMatchException.class),
                         defaultCase(throwable)
                 ));
    }

    private User fetchUser() {
        String email = request.getParameter("email");
        log.info("Start fetchUser {}", email);
        User user = userRepository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));
        log.info("End fetchUser {}", user.getUsername());
        return user;
    }

    private void validateProcess(ChangePasswordInput input, User user) {
        log.info("Start validateProcess {}", input.getEmail());

        checkIfEmailMatches(input, user);
        checkIfPasswordMatches(input, user);

        log.info("End validateProcess {}", input.getEmail());
    }

    private void checkIfPasswordMatches(ChangePasswordInput input, User user) {
        log.info("Start checkIfPasswordMatches {}", input.getEmail());

        if (!passwordEncoder.matches(input.getOldPassword(), user.getPassword())) {
            throw new PasswordDontMatchException();
        }

        log.info("End checkIfPasswordMatches {}", input.getEmail());
    }

    private void checkIfEmailMatches(ChangePasswordInput input, User user) {
        log.info("Start checkIfEmailMatches {}", input.getEmail());

        if (!user.getEmail().equals(input.getEmail())) {
            throw new EmailDontMatchException(user.getEmail(), input.getEmail());
        }

        log.info("End checkIfEmailMatches {}", input.getEmail());
    }
}
