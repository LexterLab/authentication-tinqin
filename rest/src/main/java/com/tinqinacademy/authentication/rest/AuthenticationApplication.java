package com.tinqinacademy.authentication.rest;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(scanBasePackages = {"com.tinqinacademy.authentication"})
@EntityScan(basePackages = {"com.tinqinacademy.authentication.persistence.models"})
@EnableJpaRepositories(basePackages = {"com.tinqinacademy.authentication.persistence.repositories"})
@EnableRedisRepositories(basePackages = {"com.tinqinacademy.authentication.persistence.crudrepositories"})
@EnableFeignClients(basePackages = {"com.tinqinacademy.authentication"})
public class AuthenticationApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("app.jwt-secret", dotenv.get("JWT_SECRET"));

        SpringApplication.run(AuthenticationApplication.class, args);
    }
}
