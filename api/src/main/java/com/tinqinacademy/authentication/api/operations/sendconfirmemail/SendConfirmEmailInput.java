package com.tinqinacademy.authentication.api.operations.sendconfirmemail;

import com.tinqinacademy.authentication.api.base.OperationInput;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SendConfirmEmailInput implements OperationInput {
    private String recipient;
    private String subject;
    private String code;
}
