package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamDTO;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CTFLeagueRepository;
import com.coperatecoding.secodeverseback.repository.CTFTeamRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFTeamService {
    private final CTFLeagueRepository ctfLeagueRepository;
    private final CTFTeamRepository ctfTeamRepository;
    private final UserRepository userRepository;

    public void makeTeam(User user, CTFTeamDTO.AddRequest addRequest) {

        if(user.getTeam() != null)
            throw new ForbiddenException("이미 팀이 존재하는 사용자");

        else
        {
            CTFLeague ctfLeague = ctfLeagueRepository.findById(addRequest.getLeaguePk())
                    .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않음"));

            CTFTeam team = CTFTeam.makeCTFTeam(ctfLeague, addRequest.getName(), addRequest.getPw());

            ctfTeamRepository.save(team);

            user.setTeam(team); // 유저도 자동 팀 참가
            userRepository.save(user);

        }


    }

    public CTFTeamDTO.DetailResponse getDetailTeam(User user, Long teamPk) throws NoSuchElementException {

        // 팀이 없는 유저는 확인 불가능
        if(user.getRoleType() != RoleType.ADMIN && user.getTeam() == null)
            throw new ForbiddenException("권한이 없는 사용자");
        else
        {
            CTFTeam team = ctfTeamRepository.findById(teamPk)
                    .orElseThrow(() -> new NotFoundException("해당하는 팀이 존재하지 않음"));

            CTFTeamDTO.DetailResponse detailResponse  = CTFTeamDTO.DetailResponse.builder()
                    .name(team.getName())
                    .score(team.getScore())
                    .teamRank(team.getTeamRank())
                    .build();
            return detailResponse;

        }

    }

    private Pageable makePageable(Integer page, Integer pageSize) throws RuntimeException {
        if (page == null)
            page = 1;

        if (pageSize == null)
            pageSize = 10;

        return PageRequest.of(page-1, pageSize);
    }

    public Page<CTFTeamDTO.SearchResponse> getSearchTeam(User user, Long leaguePk, int page, int pageSize) {
        Pageable pageable = makePageable(page, pageSize);

        if(user.getRoleType() == RoleType.ADMIN)
        {
//            CTFLeague league = ctfLeagueRepository.findById(leaguePk)
//                    .orElseThrow(() -> new NotFoundException("리그가 존재하지 않습니다."));

            Page<CTFTeam> teams = ctfTeamRepository.findByLeaguePk(leaguePk, pageable);
            List<CTFTeamDTO.SearchResponse> responses = teams.getContent().stream()
                    .map(team -> CTFTeamDTO.SearchResponse.builder()
                            .name(team.getName())
                            .memberList(team.getUsers().stream().map(User::getNickname).collect(Collectors.toList()))
                            .build())
                    .collect(Collectors.toList());

            return new PageImpl<>(responses, pageable, teams.getTotalElements());

        }
        else
            throw new ForbiddenException("관리자만 접근 가능합니다.");

    }

    public CTFTeamDTO.Top10ListResponse getTop10TeamList(User user, Long leaguePk) {

        List<CTFTeam> teams = ctfTeamRepository.findTop10ByLeaguePkOrderByTeamRankAsc(leaguePk);
        List<CTFTeamDTO.DetailResponse> responses = teams.stream()
                .map(team -> new CTFTeamDTO.DetailResponse(
                        team.getName(),
                        team.getScore(),
                        team.getTeamRank()))
                .collect(Collectors.toList());

        return new CTFTeamDTO.Top10ListResponse(responses.size(), responses);


    }

    public void joinTeam(User user, CTFTeamDTO.JoinRequest request) {
        CTFTeam ctfTeam = ctfTeamRepository.findByName(request.getTeamName())
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 팀이 존재하지 않습니다."));

        if(user.getTeam() == null)
        {
            user.setTeam(ctfTeam);
            userRepository.save(user);
        }
        else {
            throw new ForbiddenException("해당 유저는 ctf 팀이 존재합니다.");
        }

    }

    @Transactional(readOnly = true)
    public Boolean isExistTeamName(String teamName) {
        CTFTeam team = ctfTeamRepository.findByName(teamName)
                .orElseGet(() -> null);

        return (team == null)? false: true;
    }

    public CTFTeamDTO.TeamRankListResponse getTeamRankList(Long leaguePk) {
        CTFLeague ctfLeague = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않습니다."));

        List<CTFTeam> teams = ctfTeamRepository.findByLeagueOrderByScoreDesc(ctfLeague);

        List<CTFTeamDTO.TeamRankResponse> responses = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            CTFTeam team = teams.get(i);
            team.setRank(i + 1);
            ctfTeamRepository.save(team);

            CTFTeamDTO.TeamRankResponse teamRankResponse = CTFTeamDTO.TeamRankResponse.builder()
                    .teamName(teams.get(i).getName())
                    .rank(i + 1)
                    .build();
            responses.add(teamRankResponse);
        }

        return new CTFTeamDTO.TeamRankListResponse(responses.size(), responses);

    }
}
