package com.tinqinacademy.authentication.api.operations.getuser;

import com.tinqinacademy.authentication.api.base.OperationInput;
import com.tinqinacademy.authentication.api.base.OperationOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetUserOutput implements OperationOutput {
    private UUID id;

    @Schema(example = "domino222")
    private String username;

    @Schema(example = "domino222@gmail.com")
    private String email;

    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Smith")
    private String lastName;

    @Schema(example = "+3598323223")
    private String phoneNo;

    private Boolean isVerified;
}
