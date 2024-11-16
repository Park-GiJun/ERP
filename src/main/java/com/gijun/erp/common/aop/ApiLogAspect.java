// common/aop/ApiLogAspect.java
package com.gijun.erp.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gijun.erp.domain.log.ApiLog;
import com.gijun.erp.repository.log.ApiLogRepository;
import com.gijun.erp.security.UserSecurityAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect {

    private final ApiLogRepository apiLogRepository;
    private final ObjectMapper objectMapper;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = UUID.randomUUID().toString();
        LocalDateTime requestAt = LocalDateTime.now();
        StopWatch stopWatch = new StopWatch();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        ApiLog.ApiLogBuilder logBuilder = ApiLog.builder()
                .requestId(requestId)
                .method(request.getMethod())
                .uri(request.getRequestURI())
                .clientIp(getClientIp(request))
                .requestAt(requestAt)
                .userId(getCurrentUserId());

        // 파라미터 로깅
        if (request.getParameterMap().size() > 0) {
            String params = request.getParameterMap().entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining("&"));
            logBuilder.params(params);
        }

        // Request Body 로깅 (POST, PUT 등의 경우)
        if (isContentTypeJson(request) && hasRequestBody(request.getMethod())) {
            try {
                String requestBody = extractRequestBody(joinPoint.getArgs());
                if (requestBody != null) {
                    logBuilder.requestBody(requestBody);
                }
            } catch (Exception e) {
                log.warn("Failed to log request body", e);
            }
        }

        stopWatch.start();
        try {
            // 메서드 실행
            Object result = joinPoint.proceed();
            stopWatch.stop();

            // 성공 로그 저장
            saveSuccessLog(logBuilder, result, stopWatch.getTotalTimeMillis(), requestAt);
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            // 실패 로그 저장
            saveErrorLog(logBuilder, e, stopWatch.getTotalTimeMillis(), requestAt);
            throw e;
        }
    }

    private String extractRequestBody(Object[] args) {
        if (args.length > 0 && args[0] != null) {
            try {
                // HttpServletRequest나 유사한 컨테이너 객체는 제외
                if (!(args[0] instanceof HttpServletRequest) &&
                        !(args[0].getClass().getName().startsWith("org.springframework"))) {
                    return new ObjectMapper().writeValueAsString(args[0]);
                }
            } catch (Exception e) {
                log.warn("Failed to serialize request body", e);
            }
        }
        return null;
    }

    private boolean isContentTypeJson(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("application/json");
    }

    private boolean hasRequestBody(String method) {
        return "POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method);
    }

    private void saveSuccessLog(ApiLog.ApiLogBuilder logBuilder, Object result, long processTime, LocalDateTime requestAt) {
        try {
            String responseBody = null;
            if (result != null) {
                try {
                    responseBody = objectMapper.writeValueAsString(result);
                } catch (Exception e) {
                    log.warn("Failed to serialize response", e);
                    responseBody = "Failed to serialize response: " + e.getMessage();
                }
            }

            apiLogRepository.save(logBuilder
                    .responseBody(responseBody)
                    .statusCode(200)
                    .processTime(processTime)
                    .responseAt(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("Error saving API log", e);
        }
    }

    private void saveErrorLog(ApiLog.ApiLogBuilder logBuilder, Exception e, long processTime, LocalDateTime requestAt) {
        try {
            apiLogRepository.save(logBuilder
                    .errorMessage(e.getMessage())
                    .statusCode(500)
                    .processTime(processTime)
                    .responseAt(LocalDateTime.now())
                    .build());
        } catch (Exception ex) {
            log.error("Error saving API error log", ex);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserSecurityAdapter) {
            try {
                UserSecurityAdapter userAdapter = (UserSecurityAdapter) authentication.getPrincipal();
                return userAdapter.getUser().getId();
            } catch (Exception e) {
                log.error("Error getting current user id", e);
            }
        }
        return null;
    }
}