package com.gijun.erp.repository.attendance;

import com.gijun.erp.domain.attendance.Attendance;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND DATE(a.workDate) = DATE(:workDate)")
    Optional<Attendance> findByUserIdAndWorkDate(@Param("userId") Long userId, @Param("workDate") LocalDateTime workDate);

    List<Attendance> findByUserIdAndWorkDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    Collection<Object> findAllByWorkDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Attendance> findAllByWorkDateBetweenOrderByWorkDateDesc(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND a.workDate BETWEEN :startDate AND :endDate ORDER BY a.workDate DESC")
    Page<Attendance> findByUserAndWorkDateBetweenOrderByWorkDateDesc(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            Pageable pageable
    );


}