package com.tinqinacademy.authentication.core.configs;

import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.core.converters.impl.RegisterInputToUser;
import com.tinqinacademy.authentication.core.converters.impl.UserToGetUserOutput;
import com.tinqinacademy.authentication.core.interceptors.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RegisterInputToUser registerInputToUser;
    private final UserToGetUserOutput usertoGetUserOutput;

    private final AuthInterceptor authInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(registerInputToUser);
        registry.addConverter(usertoGetUserOutput);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(RestAPIRoutes.CHANGE_PASSWORD)
                .addPathPatterns(RestAPIRoutes.PROMOTE_USER)
                .addPathPatterns(RestAPIRoutes.DEMOTE_USER);
    }
}
