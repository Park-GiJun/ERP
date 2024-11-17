package com.gijun.erp.domain.attendance;


import com.gijun.erp.domain.attendance.enums.AttendanceStatus;
import com.gijun.erp.domain.common.BaseTimeEntity;
import com.gijun.erp.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
public class Attendance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime workDate;


    @Column(nullable = false)
    private LocalDateTime checkIn;

    @Column(nullable = true)
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column
    private String note;

    @Builder
    public Attendance(User user, LocalDateTime workDate, LocalDateTime checkIn) {
        this.user = user;
        this.workDate = workDate;
        this.checkIn = checkIn;
        this.status = AttendanceStatus.CHECKED_IN;
    }
}
