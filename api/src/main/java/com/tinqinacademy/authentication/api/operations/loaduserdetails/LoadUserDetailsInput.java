package com.tinqinacademy.authentication.api.operations.loaduserdetails;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoadUserDetailsInput implements OperationInput {
    private String username;
}
