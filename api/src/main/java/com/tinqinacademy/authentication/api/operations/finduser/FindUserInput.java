package com.tinqinacademy.authentication.api.operations.finduser;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindUserInput implements OperationInput {
    private String phoneNo;
}
