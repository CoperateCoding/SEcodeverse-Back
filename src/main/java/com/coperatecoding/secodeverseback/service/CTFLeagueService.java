package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.dto.ctf.CTFLeagueDTO;
import com.coperatecoding.secodeverseback.repository.CTFLeagueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFLeagueService {

    private final CTFLeagueRepository ctfLeagueRepository;

    public CTFLeague makeLeague(User user, CTFLeagueDTO.AddRequest addRequest) {

        CTFLeague ctfLeague = CTFLeague.makeCTFLeague(addRequest.getName(), addRequest.getOpenTime(), addRequest.getCloseTime(),
                addRequest.getMemberCnt(), addRequest.getNotice(), addRequest.getDescription());

        return ctfLeagueRepository.save(ctfLeague);

    }



}
