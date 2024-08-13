package com.tinqinacademy.authentication.api.operations.finduser;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

import java.util.UUID;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindUserOutput implements OperationOutput {
    private UUID userId;
}
