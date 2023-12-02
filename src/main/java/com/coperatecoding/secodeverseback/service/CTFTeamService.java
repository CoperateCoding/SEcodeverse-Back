package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamSortType;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamDTO;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamQuestionDTO;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CTFLeagueRepository;
import com.coperatecoding.secodeverseback.repository.CTFTeamQuestionRepository;
import com.coperatecoding.secodeverseback.repository.CTFTeamRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFTeamService {
    private final CTFLeagueRepository ctfLeagueRepository;
    private final CTFTeamRepository ctfTeamRepository;
    private final UserRepository userRepository;

    private final CTFTeamQuestionRepository ctfTeamQuestionRepository;

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

    public CTFTeamDTO.DetailResponse getDetailTeam(User user
    ) throws NoSuchElementException {

        CTFTeam ctfTeam = ctfTeamRepository.findByUsers(user)
                .orElseThrow(() -> new NotFoundException("ctf 팀이 존재하지 않습니다."));
        // 팀이 없는 유저는 확인 불가능
        if(user.getRoleType() != RoleType.ADMIN && user.getTeam() == null)
            throw new ForbiddenException("권한이 없는 사용자");
        else
        {
            CTFTeamDTO.DetailResponse detailResponse  = CTFTeamDTO.DetailResponse.builder()
                    .name(ctfTeam.getName())
                    .score((ctfTeam.getScore() != null) ? ctfTeam.getScore() : 0)
                    .teamRank(ctfTeam.getTeamRank())
                    .memberList(ctfTeam.getUsers().stream().map(User::getNickname).collect(Collectors.toList()))
                    .build();

            return detailResponse;
        }
    }

    private Pageable makePageable(CTFTeamSortType sortType, Integer page, Integer pageSize
    ) throws RuntimeException {

        Sort.Direction direction = (sortType == CTFTeamSortType.HIGH) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "score");

        int pageNumber = page != null && page > 0 ? page - 1 : 0;
        int size = pageSize != null && pageSize > 1 ? pageSize : 10;

        return PageRequest.of(pageNumber, size, sort);
    }

    public Page<CTFTeamDTO.SearchResponse> getSearchTeam(User user, Long leaguePk, int page, int pageSize, CTFTeamSortType sort)
    throws CategoryNotFoundException {
        Pageable pageable = makePageable(sort, page, pageSize);

        if(user.getRoleType() == RoleType.ADMIN)
        {
            Page<CTFTeam> teams = ctfTeamRepository.findByLeaguePk(leaguePk, pageable);
            List<CTFTeamDTO.SearchResponse> responses = teams.getContent().stream()
                    .map(team -> CTFTeamDTO.SearchResponse.builder()
                            .totalScore((team.getScore() != null) ? team.getScore() : 0)
                            .name(team.getName())
                            .memberList(team.getUsers().stream().map(User::getNickname).collect(Collectors.toList()))
                            .build())
                    .collect(Collectors.toList());

            return new PageImpl<>(responses, pageable, teams.getTotalElements());
        }
        else
            throw new ForbiddenException("관리자만 접근 가능합니다.");
    }

    public CTFTeamDTO.Top10ListResponse getTop10TeamList(Long leaguePk) {

        List<CTFTeam> teams = ctfTeamRepository.findTop10ByLeaguePkOrderByTeamRankAsc(leaguePk);
        List<CTFTeamDTO.Top10TeamResponse> responses = teams.stream()
                .map(team -> new CTFTeamDTO.Top10TeamResponse(
                        team.getName(),
                        team.getScore(),
                        (team.getScore() != null) ? team.getScore() : 0))
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

    public CTFTeamQuestionDTO.TeamScoreByCategoryListResponse getMyTeamScoresByCategory(User user) {
        CTFTeam ctfTeam = user.getTeam();

        List<Object[]> scoresByCategory = ctfTeamQuestionRepository.findTotalScoreByCategoryForTeam(ctfTeam);

        List<CTFTeamQuestionDTO.TeamScoreByCategoryResponse> list = scoresByCategory.stream()
                .map(objects -> new CTFTeamQuestionDTO.TeamScoreByCategoryResponse((String) objects[0], ((Long) objects[1]).intValue()))
                .collect(Collectors.toList());

        return new CTFTeamQuestionDTO.TeamScoreByCategoryListResponse(list.size(), list);

    }

    public boolean isExistCTFTeam(User user, Long leaguePk) {
        boolean isExist = false;

        CTFLeague league = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 리그가 존재하지 않습니다."));

        CTFTeam ctfTeam = user.getTeam();

        if(ctfTeam != null) {
            for (CTFTeam team : league.getTeamList()) {
                if (team.getPk().equals(ctfTeam.getPk())) {
                    isExist = true;
                    break;
                }
            }
        }

        return isExist;
    }
}
