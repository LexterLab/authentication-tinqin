package com.tinqinacademy.authentication.rest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.tinqinacademy.authentication"})
@EntityScan(basePackages = {"com.tinqinacademy.authentication.persistence.models"})
@EnableJpaRepositories(basePackages = {"com.tinqinacademy.authentication.persistence.repositories"})
@EnableFeignClients(basePackages = {"com.tinqinacademy.authentication"})
public class AuthenticationApplication {
}
