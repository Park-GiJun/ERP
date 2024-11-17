package com.gijun.erp.domain.attendance;

import com.gijun.erp.domain.attendance.enums.ApprovalStatus;
import com.gijun.erp.domain.attendance.enums.VacationType;
import com.gijun.erp.domain.common.BaseTimeEntity;
import com.gijun.erp.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_vacations")
@Getter
@Setter
@NoArgsConstructor
public class AttendanceVacation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacationType type;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

    @Column
    private String approverNote;

    @Builder
    public AttendanceVacation(User user, LocalDateTime startDate, LocalDateTime endDate,
                              VacationType type, String reason) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.reason = reason;
        this.approvalStatus = ApprovalStatus.PENDING;
    }
}
