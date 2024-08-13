package com.tinqinacademy.authentication.api.operations.resetpassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResetPasswordInput implements OperationInput {
    @Schema(example = "password")
    @NotBlank(message = "Field password cannot be blank")
    @Size(min = 8, max = 255, message = "Field password must be 8-255 chars")
    private String password;
    @Schema(example = "code")
    private String code;
}
