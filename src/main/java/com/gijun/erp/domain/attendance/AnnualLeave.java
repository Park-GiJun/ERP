package com.gijun.erp.domain.attendance;

import com.gijun.erp.domain.common.BaseTimeEntity;
import com.gijun.erp.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "annual_leaves")
@Getter
@NoArgsConstructor
public class AnnualLeave extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double totalDays;

    @Column(nullable = false)
    private Double usedDays;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private boolean expired;

    @Builder
    public AnnualLeave(User user, Integer year, Double totalDays) {
        this.user = user;
        this.year = year;
        this.totalDays = totalDays;
        this.usedDays = 0.0;
        this.expirationDate = LocalDate.of(year, 12, 31);
        this.expired = false;
    }

    public void addUsedDays(Double days) {
        if (getRemainingDays() < days) {
            throw new IllegalStateException("남은 연차가 부족합니다.");
        }
        this.usedDays += days;
    }

    public void subtractUsedDays(Double days) {
        this.usedDays = Math.max(0, this.usedDays - days);
    }

    public Double getRemainingDays() {
        return totalDays - usedDays;
    }

    public void expire() {
        this.expired = true;
    }
}