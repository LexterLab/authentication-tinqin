package com.tinqinacademy.authentication.core.converters.impl;

import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.core.converters.AbstractConverter;
import com.tinqinacademy.authentication.persistence.models.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterInputToUser extends AbstractConverter<RegisterInput, User> {
    @Override
    protected Class<User> getTargetClass() {
        return User.class;
    }

    @Override
    protected User doConvert(RegisterInput source) {
        User user = User
                .builder()
                .username(source.getUsername())
                .email(source.getEmail())
                .password(source.getPassword())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .phoneNo(source.getPhoneNo())
                .build();
        return user;
    }
}
