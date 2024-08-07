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
    public static final String TOKEN_EXPIRED = "Token expired: %s";
}
