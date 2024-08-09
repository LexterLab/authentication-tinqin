package com.tinqinacademy.authentication.api.operations.getuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authentication.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetUserInput implements OperationInput {
    @UUID(message = "Field username must be UUID")
    @NotBlank(message = "Field username must not be blank")
    @JsonIgnore
    private String username;
}
