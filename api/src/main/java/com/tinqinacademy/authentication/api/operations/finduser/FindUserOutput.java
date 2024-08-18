package com.tinqinacademy.authentication.api.operations.finduser;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindUserOutput implements OperationOutput {
    @Schema(example = "UUID")
    private UUID userId;
}
