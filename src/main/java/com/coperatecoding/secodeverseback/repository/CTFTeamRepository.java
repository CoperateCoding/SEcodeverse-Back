package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CTFTeamRepository extends JpaRepository<CTFTeam, Long> {
    Page<CTFTeam> findByLeaguePk(Long leaguePk, Pageable pageable);

    List<CTFTeam> findTop5ByLeaguePkOrderByTeamRankAsc(Long leaguePk);
    Optional<CTFTeam> findByName(String name);
    Optional<CTFTeam> findByUsers(User user);

    List<CTFTeam> findByLeagueOrderByScoreDesc(CTFLeague ctfLeague);
}
