package com.tinqinacademy.authentication.core.converters.impl;

import com.tinqinacademy.authentication.api.operations.getuser.GetUserOutput;
import com.tinqinacademy.authentication.core.converters.AbstractConverter;
import com.tinqinacademy.authentication.persistence.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserToGetUserOutput extends AbstractConverter<User, GetUserOutput> {
    @Override
    protected Class<GetUserOutput> getTargetClass() {
        return GetUserOutput.class;
    }

    @Override
    protected GetUserOutput doConvert(User source) {
        GetUserOutput target = GetUserOutput
                .builder()
                .id(source.getId())
                .username(source.getUsername())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .isVerified(source.getIsVerified())
                .phoneNo(source.getPhoneNo())
                .build();
        return target;
    }
}
