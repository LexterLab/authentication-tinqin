package com.tinqinacademy.authentication.restexport;

import com.tinqinacademy.authentication.api.RouteExports;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenInput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenOutput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserOutput;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetailsInput;
import com.tinqinacademy.authentication.api.operations.loaduserdetails.LoadUserDetailsOutput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenInput;
import com.tinqinacademy.authentication.api.operations.validateacesstoken.ValidateAccessTokenOutput;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
public interface AuthenticationClient {

    @RequestLine(RouteExports.VALIDATE_TOKEN)
    ValidateAccessTokenOutput validateToken(ValidateAccessTokenInput input);

    @RequestLine(RouteExports.GET_USERNAME_FROM_TOKEN)
    GetUsernameFromTokenOutput getUsernameFromToken(GetUsernameFromTokenInput input);

    @RequestLine(RouteExports.LOAD_USER_DETAILS)
    LoadUserDetailsOutput loadUserDetails(LoadUserDetailsInput input);

    @RequestLine(RouteExports.GET_USER_INFO)
    GetUserOutput getUser(@Param String username);
}
