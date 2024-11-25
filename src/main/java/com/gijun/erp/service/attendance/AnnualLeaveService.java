package com.gijun.erp.service.attendance;

import com.gijun.erp.common.exception.ResourceNotFoundException;
import com.gijun.erp.domain.attendance.AnnualLeave;
import com.gijun.erp.domain.user.User;
import com.gijun.erp.dto.attendance.AnnualLeaveDto.*;
import com.gijun.erp.repository.attendance.AnnualLeaveRepository;
import com.gijun.erp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnualLeaveService {

    private final AnnualLeaveRepository annualLeaveRepository;
    private final UserRepository userRepository;

    @Transactional
    public Response createAnnualLeave(CreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AnnualLeave annualLeave = AnnualLeave.builder()
                .user(user)
                .year(request.year())
                .totalDays(request.totalDays())
                .build();

        AnnualLeave saved = annualLeaveRepository.save(annualLeave);
        return convertToResponse(saved);
    }

    public Response getAnnualLeave(Long userId, Integer year) {
        AnnualLeave annualLeave = annualLeaveRepository.findByUserIdAndYear(userId, year)
                .orElseThrow(() -> new ResourceNotFoundException("Annual leave not found"));
        return convertToResponse(annualLeave);
    }

    @Transactional
    void useAnnualLeave(Long userId, Integer year, Double days) {
        AnnualLeave annualLeave = annualLeaveRepository.findByUserIdAndYear(userId, year)
                .orElseThrow(() -> new ResourceNotFoundException("Annual leave not found"));
        annualLeave.addUsedDays(days);
    }

    @Transactional
    void cancelAnnualLeave(Long userId, Integer year, Double days) {
        AnnualLeave annualLeave = annualLeaveRepository.findByUserIdAndYear(userId, year)
                .orElseThrow(() -> new ResourceNotFoundException("Annual leave not found"));
        annualLeave.subtractUsedDays(days);
    }

    private Response convertToResponse(AnnualLeave annualLeave) {
        return new Response(
                annualLeave.getId(),
                annualLeave.getUser().getId(),
                annualLeave.getUser().getName(),
                annualLeave.getYear(),
                annualLeave.getTotalDays(),
                annualLeave.getUsedDays(),
                annualLeave.getRemainingDays(),
                annualLeave.getExpirationDate(),
                annualLeave.isExpired(),
                annualLeave.getCreatedAt().toLocalDate(),
                annualLeave.getUpdatedAt().toLocalDate()
        );
    }
}