package com.tinqinacademy.authentication.api.operations.demoteuser;

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
public class DemoteUserInput implements OperationInput {
    @UUID(message = "Field userId must be UUID")
    @NotBlank(message = "Field userId must be present")
    @Schema(example = "UUID")
    private String userId;
}
