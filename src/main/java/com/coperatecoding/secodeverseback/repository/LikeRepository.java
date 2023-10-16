package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.board.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {

}
