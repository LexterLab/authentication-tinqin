package com.tinqinacademy.authentication.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash("tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConfirmationToken {
    @Id
    private String value;
    private String createdAt;
    @TimeToLive
    private Long expirySeconds;
    private String confirmedAt;
    private String userId;
}
