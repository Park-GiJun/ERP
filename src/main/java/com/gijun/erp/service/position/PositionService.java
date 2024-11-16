package com.gijun.erp.service.position;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;
import com.gijun.erp.domain.position.Position;
import com.gijun.erp.dto.position.PositionDto;
import com.gijun.erp.repository.position.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    @Transactional
    public PositionDto.Response createPosition(PositionDto.CreateRequest request) {
        if (positionRepository.existsByCode(request.code())) {
            throw new BaseException(ErrorCode.DUPLICATE_ENTRY, "이미 존재하는 직급 코드입니다: " + request.code());
        }

        Position position = Position.builder()
                .name(request.name())
                .code(request.code())
                .level(request.level())
                .sortOrder(request.sortOrder())
                .description(request.description())
                .build();

        Position savedPosition = positionRepository.save(position);
        return PositionDto.Response.from(savedPosition);
    }

    @Transactional(readOnly = true)
    public List<PositionDto.Response> getAllPositions() {
        return positionRepository.findAllByDeletedFalse().stream()
                .map(PositionDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PositionDto.Response getPosition(Long id) {
        Position position = positionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POSITION_NOT_FOUND));
        return PositionDto.Response.from(position);
    }

    @Transactional
    public PositionDto.Response updatePosition(Long id, PositionDto.UpdateRequest request) {
        Position position = positionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POSITION_NOT_FOUND));

        position.update(
                request.name(),
                request.code(),
                request.level(),
                request.sortOrder(),
                request.description()
        );

        return PositionDto.Response.from(position);
    }

    @Transactional
    public void deletePosition(Long id) {
        Position position = positionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BaseException(ErrorCode.POSITION_NOT_FOUND));
        position.delete();
    }
}