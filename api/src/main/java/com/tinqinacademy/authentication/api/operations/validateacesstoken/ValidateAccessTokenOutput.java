package com.tinqinacademy.authentication.api.operations.validateacesstoken;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidateAccessTokenOutput implements OperationOutput {
    private Boolean success;
}
