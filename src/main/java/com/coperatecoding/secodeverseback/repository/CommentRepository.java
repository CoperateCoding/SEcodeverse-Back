package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface CommentRepository extends JpaRepository<Comment,Long> {


}
