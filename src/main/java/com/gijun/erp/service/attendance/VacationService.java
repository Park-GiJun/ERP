package com.gijun.erp.service.attendance;

import com.gijun.erp.common.exception.UserNotFoundException;
import com.gijun.erp.common.exception.attendance.InvalidVacationRequestException;
import com.gijun.erp.common.exception.attendance.VacationNotFoundException;
import com.gijun.erp.domain.attendance.AttendanceVacation;
import com.gijun.erp.domain.attendance.enums.ApprovalStatus;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VacationService {

    private final VacationRepository vacationRepository;
    private final UserRepository userRepository;

    @Transactional
    public VacationResponse createVacation(Long userId, VacationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        validateVacationRequest(request);

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

    public VacationResponse getVacation(Long vacationId) {
        AttendanceVacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new VacationNotFoundException("Vacation not found with id: " + vacationId));

        return convertToResponse(vacation);
    }

    public Page<VacationResponse> getVacationList(Long userId, Pageable pageable) {
        Page<AttendanceVacation> vacations = vacationRepository.findByUserId(userId, pageable);
        return vacations.map(this::convertToResponse);
    }

    @Transactional
    public VacationResponse updateVacationApproval(Long vacationId, VacationApprovalRequest request) {
        AttendanceVacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new VacationNotFoundException("Vacation not found with id: " + vacationId));

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

        vacationRepository.delete(vacation);
    }

    private void validateVacationRequest(VacationRequest request) {
        if (request.startDate().isAfter(request.endDate())) {
            throw new InvalidVacationRequestException("Start date cannot be after end date");
        }

        if (request.startDate().isBefore(LocalDateTime.now())) {
            throw new InvalidVacationRequestException("Cannot request vacation for past dates");
        }
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