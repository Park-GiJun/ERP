package com.gijun.erp.repository.position;

import com.gijun.erp.domain.position.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByCode(String code);
    List<Position> findAllByDeletedFalse();
    Optional<Position> findByIdAndDeletedFalse(Long id);
}