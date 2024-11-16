package com.gijun.erp.domain.user;

import com.gijun.erp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 20)
    private String employeeNumber;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "position_id")
    private Long positionId;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    public User(String email, String password, String name, String employeeNumber,
                String phoneNumber, String role, String status,
                Long departmentId, Long positionId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.passwordChangedAt = LocalDateTime.now();
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.passwordChangedAt = LocalDateTime.now();
    }

    public void updateUserInfo(String name, String phoneNumber,
                               Long departmentId, Long positionId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.departmentId = departmentId;
        this.positionId = positionId;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void delete() {
        this.deleted = true;
    }
}
