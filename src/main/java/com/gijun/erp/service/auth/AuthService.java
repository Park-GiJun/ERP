package com.gijun.erp.service.auth;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;
import com.gijun.erp.config.jwt.JwtTokenProvider;
import com.gijun.erp.domain.department.Department;
import com.gijun.erp.domain.position.Position;
import com.gijun.erp.domain.user.User;
import com.gijun.erp.domain.user.UserRole;
import com.gijun.erp.domain.user.UserStatus;
import com.gijun.erp.dto.auth.AuthDto;
import com.gijun.erp.dto.auth.AuthDto.SignUpRequest;
import com.gijun.erp.repository.department.DepartmentRepository;
import com.gijun.erp.repository.position.PositionRepository;
import com.gijun.erp.repository.user.UserRepository;
import com.gijun.erp.security.UserSecurityAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signUp(SignUpRequest request) {
        // 1. 이메일 중복 검사
        checkDuplicateEmail(request.email());

        // 2. 비밀번호 확인 검사
        validatePassword(request.password(), request.confirmPassword());

        // 3. 부서 존재 여부 확인
        validateDepartment(request.departmentId());

        // 4. 직급 존재 여부 확인
        validatePosition(request.positionId());

        // 5. 사용자 생성
        createUser(request);

        log.info("User signed up successfully: {}", request.email());
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BaseException(
                    ErrorCode.DUPLICATE_ENTRY,
                    "이미 사용중인 이메일입니다: " + email
            );
        }
    }

    private void validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BaseException(
                    ErrorCode.INVALID_INPUT,
                    "비밀번호가 일치하지 않습니다"
            );
        }

        // 비밀번호 복잡도 검증
        if (!isPasswordValid(password)) {
            throw new BaseException(
                    ErrorCode.INVALID_INPUT,
                    "비밀번호는 8자 이상의 영문, 숫자, 특수문자를 포함해야 합니다"
            );
        }
    }

    private boolean isPasswordValid(String password) {
        // 최소 8자, 영문, 숫자, 특수문자 포함
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(passwordRegex);
    }

    private void validateDepartment(Long departmentId) {
        if (departmentId != null && !departmentRepository.existsById(departmentId)) {
            throw new BaseException(
                    ErrorCode.INVALID_INPUT,
                    "존재하지 않는 부서입니다: " + departmentId
            );
        }
    }

    private void validatePosition(Long positionId) {
        if (positionId != null && !positionRepository.existsById(positionId)) {
            throw new BaseException(
                    ErrorCode.INVALID_INPUT,
                    "존재하지 않는 직급입니다: " + positionId
            );
        }
    }

    private void createUser(SignUpRequest request) {
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .employeeNumber(request.employeeNumber())
                .phoneNumber(request.phoneNumber())
                .role(UserRole.ROLE_USER.getKey())
                .status(UserStatus.PENDING.getKey())
                .departmentId(request.departmentId())
                .positionId(request.positionId())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        // 1. 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserSecurityAdapter userAdapter = (UserSecurityAdapter) authentication.getPrincipal();
        User user = userAdapter.getUser();

        // 2. SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 토큰 생성
        String accessToken = jwtTokenProvider.createToken(authentication, false);
        String refreshToken = jwtTokenProvider.createToken(authentication, true);

        // 4. 마지막 로그인 시간 업데이트
        user.updateLastLogin();

        return new AuthDto.LoginResponse(
                accessToken,
                refreshToken,
                AuthDto.UserInfo.from(user)
        );
    }
}