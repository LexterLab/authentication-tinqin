package com.tinqinacademy.authentication.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("expired_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ExpiredJWT {
    @Id
    private String value;
    @TimeToLive
    private Long expirySeconds;
}
