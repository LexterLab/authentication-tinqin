package com.tinqinacademy.authentication.api;

public class Messages {
    public static final String RESOURCE_NOT_FOUND = "%s not found with %s : '%s'";
    public static final String INVALID_JWT_TOKEN = "Invalid JWT Token";
    public static final String EXPIRED_JWT_TOKEN = "Session expired. You have been signed out";
    public static final String UNSUPPORTED_JWT_TOKEN = "Unsupported JWT Token";
    public static final String JWT_CLAIM_EMPTY = "JWT claim string is empty";
    public static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: ";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists: %s";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists: %s";
    public static final String EMAIL_NOT_VERIFIED = "Email not verified: %s";
    public static final String TOKEN_ALREADY_CONFIRMED = "Token already confirmed: %s";
    public static final String USER_ALREADY_VERIFIED = "User already verified: %s";
    public static final String IDENTICAL_PASSWORDS = "Passwords are identical";
    public static final String PASSWORD_DONT_MATCH = "Passwords do not match";
    public static final String EMAIL_DONT_MATCH = "Emails do not match %s - %s";
    public static final String UNAUTHORIZED_USER = "Unauthorized user: %s";
    public static final String ALREADY_ADMIN = "User %s already has admin role";
}
