package com.tinqinacademy.authentication.api.operations.generateaccesstoken;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUsernameFromTokenOutput implements OperationOutput {
    private String username;
}
