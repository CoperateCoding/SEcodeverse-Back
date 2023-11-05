package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByCategory(BoardCategory category, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.category = :category AND (LOWER(b.title) LIKE %:q% OR LOWER(b.content) LIKE %:q%)")
    Page<Board> findByCategoryAndTitleOrContentContaining(@Param("category") BoardCategory category, @Param("q") String q, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE LOWER(b.title) LIKE %:q% OR LOWER(b.content) LIKE %:q%")
    Page<Board> findByTitleOrContentContaining(@Param("q") String q, Pageable pageable);

    Page<Board> findByUser(User user, Pageable pageable);
}
