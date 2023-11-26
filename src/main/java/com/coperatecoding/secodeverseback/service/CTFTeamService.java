package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CTFLeagueRepository;
import com.coperatecoding.secodeverseback.repository.CTFTeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFTeamService {
    private final CTFLeagueRepository ctfLeagueRepository;

    private final CTFTeamRepository ctfTeamRepository;

    public void makeTeam(CTFTeamDTO.AddRequest addRequest) {

        CTFLeague ctfLeague = ctfLeagueRepository.findById(addRequest.getLeaguePk())
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않음"));
        System.out.println("ㅣ여기~~" + ctfLeague.getName());

        CTFTeam team = CTFTeam.makeCTFTeam(ctfLeague, addRequest.getName(), addRequest.getPw());

        ctfTeamRepository.save(team);

    }

    public CTFTeamDTO.DetailResponse getDetailTeam(Long teamPk) throws NoSuchElementException {
        CTFTeam team = ctfTeamRepository.findById(teamPk)
                .orElseThrow(() -> new NotFoundException("해당하는 팀이 존재하지 않음"));

        CTFTeamDTO.DetailResponse detailResponse = null;

        return detailResponse;
    }
}
