package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CTFTeamQuestionRepository extends JpaRepository<CTFTeamQuestion, Long> {

    List<CTFTeamQuestion> findByCtfTeam(CTFTeam team);

    @Query("SELECT c.ctfCategory.name, SUM(c.totalScore) " +
            "FROM CTFTeamQuestion c " +
            "WHERE c.ctfTeam = :team " +
            "GROUP BY c.ctfCategory.name")
    List<Object[]> findTotalScoreByCategoryForTeam(@Param("team") CTFTeam team);
}
