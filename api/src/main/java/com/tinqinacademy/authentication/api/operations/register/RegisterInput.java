package com.tinqinacademy.authentication.api.operations.register;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class RegisterInput implements OperationInput {
    @NotBlank(message = "Field email cannot be blank")
    @Length(max = 80, min = 5, message = "Field email must be 5-80 characters")
    @Email(message = "Field email must be a valid email")
    private String email;
    @NotBlank(message = "Field username cannot be blank")
    @Length(max = 80, min = 4, message = "Field username must be 4-80 characters")
    private String username;
    @Schema(example = "Michael")
    @NotBlank(message = "Field firstName must not be empty")
    @Size(min = 2, max = 20, message = "Field firstName must be between 2-20 characters")
    private String firstName;
    @Schema(example = "Jordan")
    @NotBlank(message = "Field lastName must not be empty")
    @Size(min = 2, max = 20, message = "Field lastName must be between 2-20 characters")
    private String lastName;
    @Schema(example = "password")
    @NotBlank(message = "Field password cannot be blank")
    @Size(min = 8, max = 255, message = "Field password must be 8-255 chars")
    private String password;
    @Schema(example = "password")
    @NotBlank(message = "Field password cannot be blank")
    private String confirmPassword;
    @Schema(example = "+35984238424")
    @NotBlank(message = "Field phoneNo must not be empty")
    @Pattern(regexp = "^\\+[1-9]{1}[0-9]{3,14}$")
    private String phoneNo;
}
