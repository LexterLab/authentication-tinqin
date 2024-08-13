package com.tinqinacademy.authentication.api.operations.generateaccesstoken;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUsernameFromTokenInput implements OperationInput {
    private String token;
}
