package com.cts.rivio.modules.auth.entity;

import com.cts.rivio.modules.auth.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /*
     * Mapped as Integer for now. Once you create the Role entity, you can change this to:
     * * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "role_id", nullable = false)
     * private Role role;
     */
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private UserStatus status = UserStatus.Active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}