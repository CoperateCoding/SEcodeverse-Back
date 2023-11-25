package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    boolean existsByUserAndBoard(User user, Board board);

    Optional<Likes> findByUserAndBoard(User user, Board board);

    void deleteByUserAndBoard(User user, Board board);

//    void deleteByUsersAndBoard(User user, Board board);
}
