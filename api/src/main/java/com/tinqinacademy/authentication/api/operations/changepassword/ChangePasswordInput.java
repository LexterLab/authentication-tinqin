package com.tinqinacademy.authentication.api.operations.changepassword;

import com.tinqinacademy.authentication.api.base.OperationInput;
import com.tinqinacademy.authentication.api.validators.password.PasswordValueUnique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordValueUnique.List({
        @PasswordValueUnique(
                field = "oldPassword",
                fieldMatch = "newPassword"
        )
})
public class ChangePasswordInput implements OperationInput {
    @Schema(example = "oldPassword")
    private String oldPassword;
    @Schema(example = "newPassword")
    @NotBlank(message = "Field password cannot be blank")
    @Size(min = 8, max = 255, message = "Field password must be 8-255 chars")
    private String newPassword;
    @Email(message = "Field email must be a valid email")
    private String email;
}
