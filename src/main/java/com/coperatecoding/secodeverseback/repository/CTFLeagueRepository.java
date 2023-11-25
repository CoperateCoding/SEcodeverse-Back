package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CTFLeagueRepository extends JpaRepository<CTFLeague, Long> {
}
