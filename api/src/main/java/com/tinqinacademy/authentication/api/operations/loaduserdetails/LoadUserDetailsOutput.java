package com.tinqinacademy.authentication.api.operations.loaduserdetails;

import com.tinqinacademy.authentication.api.base.OperationOutput;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoadUserDetailsOutput implements OperationOutput {
    private User userDetails;
}
