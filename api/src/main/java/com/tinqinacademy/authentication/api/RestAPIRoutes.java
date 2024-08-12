package com.tinqinacademy.authentication.api;

public class RestAPIRoutes {
    public static final String ROOT = "/api/v1";
    public static final String AUTHENTICATE = ROOT + "/auth";
    public static final String REGISTER =  AUTHENTICATE + "/register";
    public static final String LOGIN =  AUTHENTICATE + "/login";
    public static final String CONFIRM_REGISTRATION =  AUTHENTICATE + "/confirm-registration";
    public static final String VALIDATE_TOKEN =  AUTHENTICATE + "/validate-token";
    public static final String GET_USER_FROM_TOKEN =  AUTHENTICATE + "/user";
    public static final String LOAD_USER =  AUTHENTICATE + "/load-user";
    public static final String GET_USER =  AUTHENTICATE + "/user/{username}";
    public static final String RECOVER_PASSWORD =  AUTHENTICATE + "/recover-password";
}
