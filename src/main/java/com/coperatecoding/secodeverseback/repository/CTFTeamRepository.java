package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CTFTeamRepository extends JpaRepository<CTFTeam, Long> {
}
