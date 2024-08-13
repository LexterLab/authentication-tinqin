package com.tinqinacademy.authentication.api.validators.password;

import com.tinqinacademy.authentication.api.Messages;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class )
public @interface PasswordValueUnique {
    String message() default Messages.IDENTICAL_PASSWORDS;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();

    String fieldMatch();
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        PasswordValueUnique[] value();
    }
};
