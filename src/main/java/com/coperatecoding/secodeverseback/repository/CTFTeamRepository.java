package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CTFTeamRepository extends JpaRepository<CTFTeam, Long> {
    Page<CTFTeam> findByLeaguePk(Long leaguePk, Pageable pageable);

    List<CTFTeam> findTop10ByLeaguePkOrderByTeamRankAsc(Long leaguePk);
}
