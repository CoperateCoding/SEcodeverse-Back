package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.Code;
import com.coperatecoding.secodeverseback.domain.CodeStatus;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodeRepository extends JpaRepository<Code, Long> {

    List<Code>findByStatusAndUser(CodeStatus codeStatus, User user);
    List<Code>findByUser(User user);
    Page<Code>findByUser(User user, Pageable pageable);
    Page<Code>findByStatusAndUser(CodeStatus codeStatus, User user, Pageable pageable);
}
