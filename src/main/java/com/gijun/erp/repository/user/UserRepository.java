package com.gijun.erp.repository.user;

import com.gijun.erp.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndDeletedFalse(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByEmployeeNumberAndDeletedFalse(String employeeNumber);

    @Query("SELECT u FROM User u WHERE u.departmentId = :departmentId AND u.deleted = false")
    List<User> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.deleted = false")
    List<User> findByStatus(@Param("status") String status);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:keyword% OR u.name LIKE %:keyword% AND u.deleted = false")
    List<User> searchUsers(@Param("keyword") String keyword);
}
