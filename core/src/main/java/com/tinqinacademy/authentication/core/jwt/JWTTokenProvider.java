package com.tinqinacademy.authentication.core.jwt;

import com.tinqinacademy.authentication.api.Messages;
import com.tinqinacademy.authentication.api.exceptions.JWTException;
import com.tinqinacademy.authentication.persistence.crudrepositories.ExpiredJWTRepository;
import com.tinqinacademy.authentication.persistence.models.ExpiredJWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    private final ExpiredJWTRepository expiredJWTRepository;

    public String generateToken(String username) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        Claims claims = Jwts.claims().subject(username).build();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public long getSecondsDifferenceFromExpiration(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date expiration = claims.getExpiration();

        Date now = new Date();

        long differenceInMillis = expiration.getTime() - now.getTime();
        return differenceInMillis / 1000;
    }

    public boolean validateToken(String token) {
        Optional<ExpiredJWT> expiredJWT = expiredJWTRepository.findById(token);

        if (expiredJWT.isPresent()) {
            throw new JWTException(Messages.EXPIRED_JWT_TOKEN, HttpStatus.BAD_REQUEST);
        }
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new JWTException(Messages.INVALID_JWT_TOKEN, HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException e) {
            throw new JWTException(Messages.EXPIRED_JWT_TOKEN, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException e) {
            throw new JWTException(Messages.UNSUPPORTED_JWT_TOKEN, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new JWTException(Messages.JWT_CLAIM_EMPTY, HttpStatus.BAD_REQUEST);
        }
    }
}
