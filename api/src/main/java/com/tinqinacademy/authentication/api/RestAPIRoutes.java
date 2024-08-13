package com.tinqinacademy.authentication.api;

public class RestAPIRoutes {
    public static final String ROOT = "/api/v1";
    public static final String AUTHENTICATE = ROOT + "/auth";
    public static final String USERS = ROOT + "/users";
    public static final String REGISTER =  AUTHENTICATE + "/register";
    public static final String LOGIN =  AUTHENTICATE + "/login";
    public static final String CONFIRM_REGISTRATION =  AUTHENTICATE + "/confirm-registration";
    public static final String VALIDATE_TOKEN =  AUTHENTICATE + "/validate-token";
    public static final String GET_USER_FROM_TOKEN =  AUTHENTICATE + "/user";
    public static final String LOAD_USER =  AUTHENTICATE + "/load-user";
    public static final String GET_USER =  AUTHENTICATE + "/user/{username}";
    public static final String RECOVER_PASSWORD =  AUTHENTICATE + "/recover-password";
    public static final String RESET_PASSWORD =  AUTHENTICATE + "/reset-password";
    public static final String VALIDATE_RECOVERY_CODE =  AUTHENTICATE + "/validate-recovery-code";
    public static final String CHANGE_PASSWORD =  AUTHENTICATE + "/change-password";
    public static final String PROMOTE_USER =  AUTHENTICATE + "/promote";
    public static final String DEMOTE_USER =  AUTHENTICATE + "/demote";
    public static final String LOGOUT =  AUTHENTICATE + "/logout";
    public static final String FIND_USER = USERS + "/search";
}
