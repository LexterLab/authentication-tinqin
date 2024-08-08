package com.tinqinacademy.authentication.api.operations.validateacesstoken;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NonNull
@Getter
@Setter
public class ValidateAccessTokenOutput implements OperationOutput {
    private boolean success;
}
