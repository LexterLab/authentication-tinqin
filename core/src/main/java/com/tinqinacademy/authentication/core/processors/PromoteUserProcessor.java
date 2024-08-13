package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.*;
import com.tinqinacademy.authentication.api.operations.promoteuser.PromoteUser;
import com.tinqinacademy.authentication.api.operations.promoteuser.PromoteUserInput;
import com.tinqinacademy.authentication.api.operations.promoteuser.PromoteUserOutput;
import com.tinqinacademy.authentication.persistence.models.Role;
import com.tinqinacademy.authentication.persistence.models.User;
import com.tinqinacademy.authentication.persistence.repositories.RoleRepository;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static io.vavr.API.Match;

@Service
@Slf4j
public class PromoteUserProcessor extends BaseProcessor implements PromoteUser {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HttpServletRequest request;

    public PromoteUserProcessor(ConversionService conversionService, Validator validator,
                                UserRepository userRepository, RoleRepository roleRepository,
                                HttpServletRequest request) {
        super(conversionService, validator);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.request = request;
    }

    @Override
    @Transactional
    public Either<ErrorOutput, PromoteUserOutput> process(PromoteUserInput input) {
        log.info("Start promoteUser {}", input);

        return Try.of(() -> {
            validateInput(input);

            User user = fetchCurrentUser();

            Role adminRole = fetchRole("ROLE_ADMIN");

            checkPermissions(user, adminRole);

            User userToPromote = fetchUserFromInput(input);

            checkIfUserIsAlreadyAdmin(userToPromote, adminRole);

            userToPromote.getRoles().add(adminRole);
            userRepository.save(userToPromote);

            PromoteUserOutput output = PromoteUserOutput.builder().build();

            log.info("End promoteUser {}", output);
            return output;
        }).toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        validatorCase(throwable),
                        customCase(throwable, HttpStatus.NOT_FOUND, ResourceNotFoundException.class),
                        customCase(throwable, HttpStatus.FORBIDDEN, UnauthorizedAccessException.class),
                        customCase(throwable, HttpStatus.BAD_REQUEST, MaxPermissionsException.class),
                        defaultCase(throwable)
                ));

    }

    private User fetchCurrentUser() {
        String username = (String) request.getAttribute("username");
        log.info("Start fetchUser {}", username);
        User user = userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        log.info("End fetchUser {}", user.getUsername());
        return user;
    }

    private User fetchUserFromInput(PromoteUserInput input) {
        log.info("Start fetchUserFromInput {}", input);
        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", input.getUserId()));
        log.info("End fetchUserFromInput {}", user.getUsername());
        return user;
    }

    private void checkPermissions(User user, Role role) {
        log.info("Start checkPermissions {}", user.getUsername());
        Boolean hasSufficientRights = user.getRoles().contains(role);

        if (!hasSufficientRights) {
            throw new UnauthorizedAccessException(user.getUsername());
        }

        log.info("End checkPermissions {}", user.getUsername());
    }


    private Role fetchRole(String name) {
        log.info("Start fetchRole {}", name);
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
        log.info("End fetchRole {}", role);
        return role;
    }

    private void checkIfUserIsAlreadyAdmin(User user, Role role) {
        log.info("Start checkIfUserIsAlreadyAdmin {}", user.getUsername());

        if (user.getRoles().contains(role)) {
            throw new MaxPermissionsException(user.getUsername());
        }

        log.info("End checkIfUserIsAlreadyAdmin {}", user.getUsername());

    }
}
