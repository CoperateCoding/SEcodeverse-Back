package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CTFLeagueRepository extends JpaRepository<CTFLeague, Long> {
    List<CTFLeague> findByStatus(CTFLeagueStatus open);
}
