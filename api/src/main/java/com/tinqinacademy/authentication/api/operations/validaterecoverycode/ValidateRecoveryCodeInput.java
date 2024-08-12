package com.tinqinacademy.authentication.api.operations.validaterecoverycode;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ValidateRecoveryCodeInput implements OperationInput {
    @Schema(example = "code")
    private String code;
}
