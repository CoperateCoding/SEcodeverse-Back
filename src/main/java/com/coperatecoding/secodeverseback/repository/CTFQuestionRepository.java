package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CTFQuestionRepository extends JpaRepository<CTFQuestion, Long> {
    Page<CTFQuestion> findByCategory(CTFCategory ctfCategory, Pageable pageable);

    Page<CTFQuestion> findByLeagueAndCategory(CTFLeague ctfLeague, CTFCategory ctfCategory, Pageable pageable);

    Page<CTFQuestion> findByLeague(CTFLeague ctfLeague, Pageable pageable);
}
