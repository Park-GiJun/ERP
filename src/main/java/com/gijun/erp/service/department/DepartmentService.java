package com.gijun.erp.service.department;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;
import com.gijun.erp.domain.department.Department;
import com.gijun.erp.dto.department.DepartmentDto;
import com.gijun.erp.repository.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public DepartmentDto.Response createDepartment(DepartmentDto.CreateRequest request) {
        // 부서 코드 중복 검사
        if (departmentRepository.existsByCode(request.code())) {
            throw new BaseException(ErrorCode.DUPLICATE_ENTRY, "이미 존재하는 부서 코드입니다: " + request.code());
        }

        Department department = Department.builder()
                .name(request.name())
                .code(request.code())
                .parentId(request.parentId())
                .sortOrder(request.sortOrder())
                .description(request.description())
                .build();

        Department savedDepartment = departmentRepository.save(department);
        return DepartmentDto.Response.from(savedDepartment);
    }

    @Transactional(readOnly = true)
    public List<DepartmentDto.Response> getAllDepartments() {
        return departmentRepository.findAllByDeletedFalse().stream()
                .map(DepartmentDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentDto.Response getDepartment(Long id) {
        Department department = departmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BaseException(ErrorCode.DEPARTMENT_NOT_FOUND));
        return DepartmentDto.Response.from(department);
    }

    @Transactional
    public DepartmentDto.Response updateDepartment(Long id, DepartmentDto.UpdateRequest request) {
        Department department = departmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BaseException(ErrorCode.DEPARTMENT_NOT_FOUND));

        department.update(
                request.name(),
                request.code(),
                request.parentId(),
                request.sortOrder(),
                request.description()
        );

        return DepartmentDto.Response.from(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BaseException(ErrorCode.DEPARTMENT_NOT_FOUND));
        department.delete();
    }
}