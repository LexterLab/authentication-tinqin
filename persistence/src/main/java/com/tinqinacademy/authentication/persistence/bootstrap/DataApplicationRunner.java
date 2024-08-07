package com.tinqinacademy.authentication.persistence.bootstrap;

import com.tinqinacademy.authentication.persistence.models.Role;
import com.tinqinacademy.authentication.persistence.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class DataApplicationRunner implements ApplicationRunner {
    private final RoleRepository roleRepository;
    @Override
    public void run(ApplicationArguments args) {
        if (roleRepository.count() == 0) {
            Role user = Role.builder()
                    .name("ROLE_USER")
                    .build();

            Role admin = Role.builder()
                    .name("ROLE_ADMIN")
                    .build();

            roleRepository.saveAll(List.of(user, admin));
        }

    }
}
