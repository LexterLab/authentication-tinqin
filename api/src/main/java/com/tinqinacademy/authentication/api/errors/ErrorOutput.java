package com.tinqinacademy.authentication.api.errors;

import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ErrorOutput {
    private List<Error> errors;
    private HttpStatusCode statusCode;
}
