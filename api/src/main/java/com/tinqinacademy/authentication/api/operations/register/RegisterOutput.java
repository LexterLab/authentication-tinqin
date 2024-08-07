package com.tinqinacademy.authentication.api.operations.register;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterOutput implements OperationOutput {
    private UUID id;
}
