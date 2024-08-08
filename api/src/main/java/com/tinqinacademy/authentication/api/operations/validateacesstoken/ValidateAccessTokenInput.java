package com.tinqinacademy.authentication.api.operations.validateacesstoken;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ValidateAccessTokenInput implements OperationInput {
    private String accessToken;
}
