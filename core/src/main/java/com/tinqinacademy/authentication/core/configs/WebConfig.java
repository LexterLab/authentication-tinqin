package com.tinqinacademy.authentication.core.configs;

import com.tinqinacademy.authentication.core.converters.impl.RegisterInputToUser;
import com.tinqinacademy.authentication.core.converters.impl.UserToGetUserOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RegisterInputToUser registerInputToUser;
    private final UserToGetUserOutput usertoGetUserOutput;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(registerInputToUser);
        registry.addConverter(usertoGetUserOutput);
    }
}
