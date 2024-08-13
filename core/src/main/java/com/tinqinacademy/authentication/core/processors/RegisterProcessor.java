package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.EmailAlreadyExistsException;
import com.tinqinacademy.authentication.api.exceptions.PhoneNoExistsException;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.exceptions.UsernameAlreadyExistException;
import com.tinqinacademy.authentication.api.operations.register.Register;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterOutput;
import com.tinqinacademy.authentication.persistence.models.ConfirmationToken;
import com.tinqinacademy.authentication.persistence.models.Role;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.crudrepositories.ConfirmationTokenRepository;
import com.tinqinacademy.authentication.persistence.repositories.RoleRepository;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import com.tinqinacademy.emails.api.operations.sendconfirmemail.SendConfirmEmailInput;
import com.tinqinacademy.emails.restexport.EmailClient;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class RegisterProcessor extends BaseProcessor implements Register {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailClient emailClient;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public RegisterProcessor(ConversionService conversionService, Validator validator, UserRepository userRepository,
                             RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                             EmailClient emailClient, ConfirmationTokenRepository confirmationTokenRepository) {
        super(conversionService, validator);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailClient = emailClient;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    @Async
    @Transactional
    public Either<ErrorOutput, RegisterOutput> process(RegisterInput input) {
        log.info("Start register {}", input.getUsername());
        return Try.of(() -> {
            validateInput(input);
            checkForExistingUser(input);
            String encodedPassword = passwordEncoder.encode(input.getPassword());
            input.setPassword(encodedPassword);
            User user = conversionService.convert(input, User.class);
            user = setUpUserRoles(user);
            userRepository.save(user);
            RegisterOutput output = RegisterOutput.builder()
                    .id(user.getId())
                    .build();

            emailClient.sendConfirmEmail(buildEmail(user));


            log.info("End register {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        validatorCase(throwable),
                        customCase(throwable, HttpStatus.BAD_REQUEST, EmailAlreadyExistsException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, UsernameAlreadyExistException.class),
                        feignCase(throwable),
                        defaultCase(throwable)
                ));
    }

    private User setUpUserRoles(User user) {
        log.info("Start setUpUserRoles {}", user.getUsername());
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));
        user.setRoles(Set.of(role));
        log.info("End setUpUserRoles {}", user.getUsername());
        return user;
    }

    private void checkForExistingUser(RegisterInput input) {
        log.info("Start checkForExistingUser {}", input.getUsername());
        boolean existsEmail = userRepository.existsByEmailIgnoreCase(input.getEmail());
        boolean existsUsername = userRepository.existsByUsernameIgnoreCase(input.getUsername());
        boolean existsPhoneNo = userRepository.existsByPhoneNo(input.getPhoneNo());
        if (existsEmail) {
            throw new EmailAlreadyExistsException(input.getEmail());
        }
        if (existsUsername) {
            throw new UsernameAlreadyExistException(input.getUsername());
        }

        if (existsPhoneNo) {
            throw new PhoneNoExistsException(input.getPhoneNo());
        }
        log.info("End checkForExistingUser {}", input.getUsername());

    }

    private SendConfirmEmailInput buildEmail(User user) {
        log.info("Start buildEmail {}", user.getUsername());
      SendConfirmEmailInput email = SendConfirmEmailInput
                .builder()
                .code(createConfirmationToken(user))
                .recipient(user.getEmail())
                .build();
        log.info("End buildEmail {}", user.getUsername());
        return email;
    }

    private String createConfirmationToken(User user) {
        log.info("Start createConfirmationToken {}", user.getUsername());
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .createdAt(LocalDateTime.now().toString())
                .value(token)
                .expirySeconds(900L)
                .userId(user.getId().toString())
                .build();
        confirmationTokenRepository.save(confirmationToken);
        log.info("End createConfirmationToken {}", confirmationToken);
        return token;
    }
}
