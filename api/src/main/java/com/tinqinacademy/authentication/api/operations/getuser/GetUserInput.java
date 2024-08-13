package com.tinqinacademy.authentication.api.operations.getuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetUserInput implements OperationInput {
    @NotBlank(message = "Field username must not be blank")
    @JsonIgnore
    private String username;
}
