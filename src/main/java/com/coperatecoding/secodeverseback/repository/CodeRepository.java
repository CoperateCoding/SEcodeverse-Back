package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.Code;
import com.coperatecoding.secodeverseback.domain.CodeStatus;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CodeRepository extends JpaRepository<Code, Long> {

    List<Code>findByStatusAndUser(CodeStatus codeStatus, User user);
    List<Code>findByUser(User user);
}
