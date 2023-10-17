package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);


}
