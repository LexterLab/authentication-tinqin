package com.tinqinacademy.authentication.api.operations.promoteuser;

import com.tinqinacademy.authentication.api.base.OperationInput;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PromoteUserInput implements OperationInput {
    @UUID(message = "Field userId must be UUID")
    @NotBlank(message = "Field userId must be present")
    @Schema(example = "UUID")
    private String userId;
}
