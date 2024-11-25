package com.gijun.erp.repository.attendance;

import com.gijun.erp.domain.attendance.AnnualLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnnualLeaveRepository extends JpaRepository<AnnualLeave, Long> {
    Optional<AnnualLeave> findByUserIdAndYear(Long userId, Integer year);
}