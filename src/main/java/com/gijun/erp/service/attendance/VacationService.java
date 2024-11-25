// service/attendance/VacationService.java
package com.gijun.erp.service.attendance;

import com.gijun.erp.common.exception.UserNotFoundException;
import com.gijun.erp.common.exception.attendance.InvalidVacationRequestException;
import com.gijun.erp.common.exception.attendance.VacationNotFoundException;
import com.gijun.erp.domain.attendance.AttendanceVacation;
import com.gijun.erp.domain.attendance.enums.ApprovalStatus;
import com.gijun.erp.domain.attendance.enums.VacationType;
import com.gijun.erp.domain.user.User;
import com.gijun.erp.dto.attendance.AttendanceVacationDto.VacationRequest;
import com.gijun.erp.dto.attendance.AttendanceVacationDto.VacationResponse;
import com.gijun.erp.dto.attendance.AttendanceVacationDto.VacationApprovalRequest;
import com.gijun.erp.repository.attendance.VacationRepository;
import com.gijun.erp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationService {

    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;
    private final AnnualLeaveService annualLeaveService;

    @Transactional
    public VacationResponse createVacation(Long userId, VacationRequest request) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 휴가 신청 유효성 검증
        validateVacationRequest(request);

        // 휴가 일수 계산
        double vacationDays = calculateVacationDays(request.startDate(), request.endDate(), request.type());

        // 연차 차감 가능 여부 확인 (연차/반차인 경우에만)
        if (isAnnualLeaveType(request.type())) {
            // 현재 연도의 연차 정보 확인 및 차감
            int year = LocalDate.now().getYear();
            annualLeaveService.useAnnualLeave(userId, year, vacationDays);
        }

        // 휴가 신청 정보 저장
        AttendanceVacation vacation = AttendanceVacation.builder()
                .user(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .type(request.type())
                .reason(request.reason())
                .build();

        AttendanceVacation savedVacation = vacationRepository.save(vacation);
        return convertToResponse(savedVacation);
    }

    @Transactional
    public VacationResponse updateVacationApproval(Long vacationId, VacationApprovalRequest request) {
        AttendanceVacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new VacationNotFoundException("Vacation not found with id: " + vacationId));

        // 이전 상태가 대기 중이고, 승인 거절로 변경되는 경우 연차 반환
        if (vacation.getApprovalStatus() == ApprovalStatus.PENDING
                && request.approvalStatus() == ApprovalStatus.REJECTED
                && isAnnualLeaveType(vacation.getType())) {

            double vacationDays = calculateVacationDays(
                    vacation.getStartDate(),
                    vacation.getEndDate(),
                    vacation.getType()
            );

            int year = vacation.getStartDate().getYear();
            annualLeaveService.cancelAnnualLeave(vacation.getUser().getId(), year, vacationDays);
        }

        vacation.setApprovalStatus(request.approvalStatus());
        vacation.setApproverNote(request.approverNote());

        return convertToResponse(vacation);
    }

    @Transactional
    public void deleteVacation(Long vacationId) {
        AttendanceVacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new VacationNotFoundException("Vacation not found with id: " + vacationId));

        if (vacation.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new InvalidVacationRequestException("Cannot delete vacation that is already " +
                    vacation.getApprovalStatus().getDescription());
        }

        // 연차/반차인 경우 차감했던 일수 반환
        if (isAnnualLeaveType(vacation.getType())) {
            double vacationDays = calculateVacationDays(
                    vacation.getStartDate(),
                    vacation.getEndDate(),
                    vacation.getType()
            );

            int year = vacation.getStartDate().getYear();
            annualLeaveService.cancelAnnualLeave(vacation.getUser().getId(), year, vacationDays);
        }

        vacationRepository.delete(vacation);
    }

    // 기존 메서드들...
    public VacationResponse getVacation(Long vacationId) {
        AttendanceVacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new VacationNotFoundException("Vacation not found with id: " + vacationId));

        return convertToResponse(vacation);
    }

    public Page<VacationResponse> getVacationList(Long userId, Pageable pageable) {
        Page<AttendanceVacation> vacations = vacationRepository.findByUserId(userId, pageable);
        return vacations.map(this::convertToResponse);
    }

    // 유틸리티 메서드들
    private void validateVacationRequest(VacationRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
            throw new InvalidVacationRequestException("Start date cannot be after end date");
        }

        if (request.startDate().isBefore(LocalDateTime.now())) {
            throw new InvalidVacationRequestException("Cannot request vacation for past dates");
        }

        // 반차의 경우 시작일과 종료일이 같아야 함
        if ((request.type() == VacationType.HALF_DAY_AM || request.type() == VacationType.HALF_DAY_PM)
                && !request.startDate().toLocalDate().equals(request.endDate().toLocalDate())) {
            throw new InvalidVacationRequestException("Half-day vacation must be within the same day");
        }
    }

    private boolean isAnnualLeaveType(VacationType type) {
        return type == VacationType.ANNUAL
                || type == VacationType.HALF_DAY_AM
                || type == VacationType.HALF_DAY_PM;
    }

    private double calculateVacationDays(LocalDateTime startDate, LocalDateTime endDate, VacationType type) {
        if (type == VacationType.HALF_DAY_AM || type == VacationType.HALF_DAY_PM) {
            return 0.5;
        }

        return ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
    }

    private VacationResponse convertToResponse(AttendanceVacation vacation) {
        return new VacationResponse(
                vacation.getId(),
                vacation.getUser().getId(),
                vacation.getUser().getName(),
                vacation.getStartDate(),
                vacation.getEndDate(),
                vacation.getType(),
                vacation.getReason(),
                vacation.getApprovalStatus(),
                vacation.getApproverNote(),
                vacation.getCreatedAt(),
                vacation.getUpdatedAt()
        );
    }
}