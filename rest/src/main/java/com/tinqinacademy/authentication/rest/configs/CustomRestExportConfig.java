package com.tinqinacademy.authentication.rest.configs;

import com.tinqinacademy.authentication.api.RouteExports;
import com.tinqinacademy.restexportprocessor.main.RestExportConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@RestExportConfig(destination = RouteExports.CLIENT, clientName = "AuthenticationClient")
public class CustomRestExportConfig {
}
