package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
import com.coperatecoding.secodeverseback.dto.ctf.CTFLeagueDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CTFLeagueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFLeagueService {

    private final CTFLeagueRepository ctfLeagueRepository;

    public void makeLeague(CTFLeagueDTO.AddLeagueRequest addRequest) {

        CTFLeague league = CTFLeague.builder()
                .name(addRequest.getName())
                .openTime(addRequest.getOpenTime())
                .closeTime(addRequest.getCloseTime())
                .memberCnt(addRequest.getMemberCnt())
                .notice(addRequest.getNotice())
                .description(addRequest.getDescription())
                .build();

        ctfLeagueRepository.save(league);
    }


    public CTFLeagueDTO.CTFLeagueDetailResponse getDetailLeague(Long leaguePk) throws NoSuchElementException {
        CTFLeague league = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않음"));

        CTFLeagueDTO.CTFLeagueDetailResponse detailResponse = CTFLeagueDTO.CTFLeagueDetailResponse.builder()
                .name(league.getName())
                .openTime(league.convertDate(league.getOpenTime()))
                .closeTime(league.convertDate(league.getCloseTime()))
                .memberCnt(league.getMemberCnt())
                .notice(league.getNotice())
                .description(league.getDescription())
                .status(league.checkLeagueStatus())
                .build();

        return detailResponse;
    }

    public void editCTFLeague(Long leaguePk, CTFLeagueDTO.EditRequest request) throws RuntimeException {
        CTFLeague league = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않음"));

        league.edit(request.getName(), request.getOpenTime(), request.getCloseTime(), request.getMemberCnt(), request.getNotice(), request.getDescription());

    }

    public CTFLeagueDTO.StatusResponse getCTFLeagueStatus(Long leaguePk) {
        CTFLeague league = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않음"));

        CTFLeagueStatus ctfLeagueStatus = league.checkLeagueStatus();

        CTFLeagueDTO.StatusResponse statusResponse = CTFLeagueDTO.StatusResponse.builder()
                .ctfLeagueStatus(ctfLeagueStatus)
                .build();

        ctfLeagueRepository.save(league);
        return statusResponse;
    }

    public void deleteCTFLeague(Long leaguePk) throws RuntimeException {

        CTFLeague league = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않음"));

        ctfLeagueRepository.delete(league);

    }

    private Pageable makePageable(Integer page, Integer pageSize) throws RuntimeException {

        Sort sort = Sort.by(Sort.Direction.DESC, "openTime");
        if (page == null)
            page = 1;

        if (pageSize == null)
            pageSize = 10;

        return PageRequest.of(page-1, pageSize, sort);
    }


    public Page<CTFLeagueDTO.BriefResponse> getCTFLeagueAll(int page, int pageSize) throws RuntimeException {
        Pageable pageable = makePageable(page, pageSize);
        Page<CTFLeague> list;

        list = ctfLeagueRepository.findAll(pageable);

        List<CTFLeagueDTO.BriefResponse> briefResponseList = list.getContent().stream()
                .map(ctfLeague -> CTFLeagueDTO.BriefResponse.builder()
                        .name(ctfLeague.getName())
                        .status(ctfLeague.getStatus())
                        .openTime(ctfLeague.convertDate(ctfLeague.getOpenTime()))
                        .closeTime(ctfLeague.convertDate(ctfLeague.getCloseTime()))
                        .build()
                ).collect(Collectors.toList());

        return new PageImpl<>(briefResponseList, pageable, list.getTotalElements());

    }

    public Long getOngoingLeague() throws RuntimeException {
        List<CTFLeague> ongoingLeagues = ctfLeagueRepository.findByStatus(CTFLeagueStatus.OPEN);
        if (ongoingLeagues.isEmpty()) {
            throw new RuntimeException("현재 진행 중인 리그가 없습니다.");
        }
        // 여러 개의 진행 중인 리그 중 첫 번째 리그의 pk를 반환
        return ongoingLeagues.get(0).getPk();

    }
}
