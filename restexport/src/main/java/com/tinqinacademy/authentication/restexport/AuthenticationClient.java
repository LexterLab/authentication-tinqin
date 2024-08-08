package com.tinqinacademy.authentication.restexport;

import com.tinqinacademy.authentication.api.RouteExports;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenInput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenOutput;
import feign.Headers;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
public interface AuthenticationClient {

    @RequestLine(RouteExports.VALIDATE_TOKEN)
    ValidateAccessTokenOutput validateToken(ValidateAccessTokenInput input);
}
