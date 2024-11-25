package com.gijun.erp.service.attendance;

import com.gijun.erp.common.exception.ErrorCode;
import com.gijun.erp.common.exception.attendance.AttendanceException;
import com.gijun.erp.domain.attendance.Attendance;
import com.gijun.erp.dto.attendance.AttendanceDto.*;
import com.gijun.erp.repository.attendance.AttendanceRepository;
import com.gijun.erp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gijun.erp.domain.user.User;
import com.gijun.erp.domain.attendance.enums.AttendanceStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    @Transactional
    public AttendanceResponse checkIn(Long userId, CheckInRequest request) {
        User user = userService.findById(userId);
        LocalDateTime now = LocalDateTime.now();

        // 이미 출근했는지 확인
        attendanceRepository.findByUserIdAndWorkDate(userId, now)
                .ifPresent(a -> {
                    throw new AttendanceException(ErrorCode.ALREADY_CHECKED_IN);
                });

        Attendance attendance = Attendance.builder()
                .user(user)
                .workDate(now)
                .checkIn(now)
                .build();

        if (request.note() != null) {
            attendance.setNote(request.note());
        }

        return convertToResponse(attendanceRepository.save(attendance));
    }

    @Transactional
    public AttendanceResponse checkOut(Long userId, CheckOutRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Attendance attendance = attendanceRepository.findByUserIdAndWorkDate(userId, now)
                .orElseThrow(() -> new AttendanceException(ErrorCode.NO_CHECK_IN_RECORD));

        attendance.setCheckOut(now);
        attendance.setStatus(AttendanceStatus.CHECKED_OUT);

        if (request.note() != null) {
            attendance.setNote(request.note());
        }

        return convertToResponse(attendanceRepository.save(attendance));
    }


    private AttendanceResponse convertToResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getUser().getId(),
                attendance.getUser().getName(),
                attendance.getWorkDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut(),
                attendance.getStatus(),
                attendance.getNote(),
                attendance.getCreatedAt(),
                attendance.getUpdatedAt()
        );
    }

    public Page<AttendanceResponse> searchAttendances(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ) {
        return attendanceRepository
                .findAllByWorkDateBetweenOrderByWorkDateDesc(startDate, endDate, pageable)
                .map(this::convertToResponse);
    }

    public Page<AttendanceResponse> searchAttendancesSolo(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long userId,
            Pageable pageable
    ) {
        return attendanceRepository
                .findByUserAndWorkDateBetweenOrderByWorkDateDesc(startDate, endDate,userId, pageable)
                .map(this::convertToResponse);
    }
}