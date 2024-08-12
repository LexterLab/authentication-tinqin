package com.tinqinacademy.authentication.api.operations.recoverpassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecoverPasswordInput implements OperationInput {
    @Schema(example = "domino222@gmail.com")
    private String email;
}
