// controller/UserController.java
package com.gijun.erp.controller.user;

import com.gijun.erp.common.response.ApiResponse;
import com.gijun.erp.domain.user.User;
import com.gijun.erp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  // Add base path
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/me")  // Fixed path
    public ApiResponse<User> getProfile() {
        return ApiResponse.success(userService.getCurrentUser());
    }

    @PutMapping("/users/me")  // Fixed path
    public ApiResponse<User> updateProfile(@RequestBody UpdateProfileRequest request) {
        User updatedUser = userService.updateProfile(request);
        return ApiResponse.success(updatedUser);
    }

    @PutMapping("/users/me/password")  // Fixed path
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ApiResponse.successResponse();
    }

    public record UpdateProfileRequest(
            String name,
            String phoneNumber
    ) {}

    public record ChangePasswordRequest(
            String currentPassword,
            String newPassword
    ) {}
}