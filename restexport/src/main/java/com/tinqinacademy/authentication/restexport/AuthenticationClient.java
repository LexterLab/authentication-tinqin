package com.tinqinacademy.authentication.restexport;

import com.tinqinacademy.authentication.api.RouteExports;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenInput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenOutput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenInput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenOutput;
import feign.Headers;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
public interface AuthenticationClient {

    @RequestLine(RouteExports.VALIDATE_TOKEN)
    ValidateAccessTokenOutput validateToken(ValidateAccessTokenInput input);

    @RequestLine(RouteExports.GET_USERNAME_FROM_TOKEN)
    GetUsernameFromTokenOutput getUsernameFromToken(GetUsernameFromTokenInput input);
}
