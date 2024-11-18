package com.gijun.erp.service.user;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;
import com.gijun.erp.common.exception.UserNotFoundException;
import com.gijun.erp.controller.user.UserController;
import com.gijun.erp.domain.user.User;
import com.gijun.erp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public User updateProfile(UserController.UpdateProfileRequest request) {
        User user = getCurrentUser();
        user.updateUserInfo(
                request.name(),
                request.phoneNumber(),
                user.getDepartmentId(),
                user.getPositionId()
        );
        return user;
    }

    @Transactional
    public void changePassword(UserController.ChangePasswordRequest request) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(request.newPassword()));
    }
}