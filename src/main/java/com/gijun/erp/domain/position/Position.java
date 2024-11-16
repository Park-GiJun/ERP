package com.gijun.erp.domain.position;

import com.gijun.erp.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "positions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Position extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String code;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false)
    private int sortOrder;

    @Column(length = 255)
    private String description;

    @Builder
    public Position(String name, String code, int level, int sortOrder, String description) {
        this.name = name;
        this.code = code;
        this.level = level;
        this.sortOrder = sortOrder;
        this.description = description;
    }

    public void update(String name, String code, int level, int sortOrder, String description) {
        this.name = name;
        this.code = code;
        this.level = level;
        this.sortOrder = sortOrder;
        this.description = description;
    }

    public void delete() {
        this.deleted = true;
    }
}