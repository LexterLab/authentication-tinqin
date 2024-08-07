package com.tinqinacademy.authentication.persistence.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Role {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
}
