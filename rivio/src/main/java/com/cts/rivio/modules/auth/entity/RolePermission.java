package com.cts.rivio.modules.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_permissions")
@IdClass(RolePermissionId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    /*
     * Mapped as Integer for now. Once relationships are fully established,
     * this is typically mapped as:
     * @Id
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "role_id", nullable = false)
     * private Role role;
     */
    @Id
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    /*
     * Mapped as Integer for now.
     * @Id
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name = "permission_id", nullable = false)
     * private Permission permission;
     */
    @Id
    @Column(name = "permission_id", nullable = false)
    private Integer permissionId;
}