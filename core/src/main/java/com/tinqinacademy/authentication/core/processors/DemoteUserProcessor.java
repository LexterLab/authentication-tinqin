package com.tinqinacademy.authentication.core.processors;

import com.tinqinacademy.authentication.api.errors.ErrorOutput;
import com.tinqinacademy.authentication.api.exceptions.*;
import com.tinqinacademy.authentication.api.operations.demoteuser.DemoteUser;
import com.tinqinacademy.authentication.api.operations.demoteuser.DemoteUserInput;
import com.tinqinacademy.authentication.api.operations.demoteuser.DemoteUserOutput;
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
public class DemoteUserProcessor extends BaseProcessor implements DemoteUser {
    private final HttpServletRequest httpServletRequest;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DemoteUserProcessor(ConversionService conversionService, Validator validator,
                               HttpServletRequest httpServletRequest, UserRepository userRepository, RoleRepository roleRepository) {
        super(conversionService, validator);
        this.httpServletRequest = httpServletRequest;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Either<ErrorOutput, DemoteUserOutput> process(DemoteUserInput input) {
        log.info("Start demoteUser {}", input);

        return Try.of(() -> {
            validateInput(input);

            Role adminRole = fetchRole("ROLE_ADMIN");
            checkIfThereAreMinNumberOfAdmins(adminRole);

            User user = fetchCurrentUser();
            preventSelfDemotion(user, input);

            checkPermissions(user, adminRole);

            User userToDemote = fetchUserFromInput(input);
            checkIfUserIsAdmin(userToDemote, adminRole);

            userToDemote.getRoles().remove(adminRole);
            userRepository.save(userToDemote);

            DemoteUserOutput output = DemoteUserOutput.builder().build();

            log.info("End demoteUser {}", output);
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

    private void checkIfThereAreMinNumberOfAdmins(Role role) {
        log.info("Start checkIfThereAreMinNumberOfAdmins {}", role);
        Long numberOfAdmins = userRepository.countUserByRolesContaining(role);

        if (numberOfAdmins < 2) {
            throw new MinNumberOfAdminsException();
        }

        log.info("End checkIfThereAreMinNumberOfAdmins {}", numberOfAdmins);
    }

    private User fetchCurrentUser() {
        String username = (String) httpServletRequest.getAttribute("username");
        log.info("Start fetchUser {}", username);
        User user = userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        log.info("End fetchUser {}", user.getUsername());
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

    private User fetchUserFromInput(DemoteUserInput input) {
        log.info("Start fetchUserFromInput {}", input);
        User user = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", input.getUserId()));
        log.info("End fetchUserFromInput {}", user.getUsername());
        return user;
    }

    private void checkIfUserIsAdmin(User user, Role role) {
        log.info("Start checkIfUserIsAlreadyAdmin {}", user.getUsername());

        if (!user.getRoles().contains(role)) {
            throw new MinPermissionsException(user.getUsername());
        }

        log.info("End checkIfUserIsAlreadyAdmin {}", user.getUsername());
    }

    private void preventSelfDemotion(User user, DemoteUserInput input) {
        log.info("Start preventSelfDemotion {}", user.getUsername());

        if (user.getId().toString().equals(input.getUserId())) {
            throw new SelfRoleControlException();
        }

        log.info("End preventSelfDemotion {}", user.getUsername());
    }

}
