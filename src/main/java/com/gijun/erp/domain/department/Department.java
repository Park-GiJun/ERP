package com.gijun.erp.domain.department;

import com.gijun.erp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false)
    private int sortOrder;

    @Column(length = 255)
    private String description;

    @Builder
    public Department(String name, String code, Long parentId, int sortOrder, String description) {
        this.name = name;
        this.code = code;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.description = description;
    }

    public void update(String name, String code, Long parentId, int sortOrder, String description) {
        this.name = name;
        this.code = code;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.description = description;
    }

    public void delete() {
        this.deleted = true;
    }
}