package com.gijun.erp.security;

import com.gijun.erp.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {

    public static Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof UserSecurityAdapter) {
                UserSecurityAdapter userAdapter = (UserSecurityAdapter) authentication.getPrincipal();
                return userAdapter.getUser().getId();
            }
        } catch (Exception e) {
            log.error("Error getting current user id", e);
        }
        return null;
    }

    public static User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && authentication.getPrincipal() instanceof UserSecurityAdapter) {
                UserSecurityAdapter userAdapter = (UserSecurityAdapter) authentication.getPrincipal();
                return userAdapter.getUser();
            }
        } catch (Exception e) {
            log.error("Error getting current user", e);
        }
        return null;
    }
}
