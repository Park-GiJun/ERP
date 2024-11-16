package com.gijun.erp.domain.log;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_logs")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "method", nullable = false, length = 10)
    private String method;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "params", columnDefinition = "TEXT")
    private String params;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "client_ip", length = 50)
    private String clientIp;

    @Column(name = "process_time")
    private Long processTime;  // 처리시간 (milliseconds)

    @Column(name = "request_at", nullable = false)
    private LocalDateTime requestAt;

    @Column(name = "response_at")
    private LocalDateTime responseAt;

    @Builder
    public ApiLog(
            String requestId,
            Long userId,
            String method,
            String uri,
            String params,
            String requestBody,
            String responseBody,
            Integer statusCode,
            String errorMessage,
            String clientIp,
            Long processTime,
            LocalDateTime requestAt,
            LocalDateTime responseAt
    ) {
        this.requestId = requestId;
        this.userId = userId;
        this.method = method;
        this.uri = uri;
        this.params = params;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.clientIp = clientIp;
        this.processTime = processTime;
        this.requestAt = requestAt;
        this.responseAt = responseAt;
    }
}