package com.gijun.erp.repository.attendance;

import com.gijun.erp.domain.attendance.AttendanceVacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<AttendanceVacation, Long> {
    Page<AttendanceVacation> findByUserId(Long userId, Pageable pageable);
}
