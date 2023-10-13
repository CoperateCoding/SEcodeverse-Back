package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    Optional<BoardCategory> findByName(String name);
}
