package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamSortType;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamDTO;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamQuestionDTO;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.service.CTFTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Tag(name = "CTF팀", description = "CTF 팀 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class CTFTeamController {

    private final CTFTeamService ctfTeamService;

    @Operation(summary = "ctf 팀 등록")
    @PostMapping("/ctf/team/post")
    public ResponseEntity makeCTFTeam(@AuthenticationPrincipal User user,
                                      @RequestBody @Valid CTFTeamDTO.AddRequest addRequest
    ) throws IllegalArgumentException, InvalidDataAccessApiUsageException {

        ctfTeamService.makeTeam(user, addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "유저가 ctf 팀이 있는지 확인",
            description = "ctf 팀이 현재 리그에 존재할때만 true 반환")
    @GetMapping("/ctf/team/user/isexist")
    public ResponseEntity<Boolean> isExistCTFTeam(@AuthenticationPrincipal User user,
                                                  @RequestParam Long leaguePk
    ) throws NoSuchElementException {
        boolean isExist = ctfTeamService.isExistCTFTeam(user, leaguePk);
        return ResponseEntity.ok(isExist);
    }

    @Operation(
            summary = "ctf 팀 상세 조회",
            description = "관리자 or 유저의 팀이 있을때만 가능"
    )
    @GetMapping("/ctf/detail/my/team")
    public ResponseEntity<CTFTeamDTO.DetailResponse> getCtfTeam(@AuthenticationPrincipal User user
    ) throws NoSuchElementException {

        CTFTeamDTO.DetailResponse detailResponse = ctfTeamService.getDetailTeam(user);

        return ResponseEntity.ok(detailResponse);
    }

    @Operation(summary = "ctf 팀 참가")
    @PostMapping("/ctf/team/join")
    public ResponseEntity joinCTFTeam(@AuthenticationPrincipal User user,
                                      @RequestBody @Valid CTFTeamDTO.JoinRequest request
    ) throws IllegalArgumentException, InvalidDataAccessApiUsageException {

        ctfTeamService.joinTeam(user, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "모든 팀 정보 조회", description = """
    팀 이름, 팀원 닉네임
    관리자만 가능
    """)
    @GetMapping("/admin/ctf/team/all/{leaguePk}")
    public ResponseEntity<CTFTeamDTO.SearchListResponse> getSearchTeamList(
            @AuthenticationPrincipal User user,
            @PathVariable Long leaguePk,
            @RequestParam(required = false, defaultValue = "1")
            @Min(value = 1, message = "page는 1보다 커야합니다") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "pageSize는 1보다 커야합니다") int pageSize,
            @RequestParam(required = false, defaultValue = "HIGH") CTFTeamSortType sort)
    throws CategoryNotFoundException {

        Page<CTFTeamDTO.SearchResponse> ctfPage = ctfTeamService.getSearchTeam(user, leaguePk, page, pageSize, sort);

        CTFTeamDTO.SearchListResponse response = CTFTeamDTO.SearchListResponse.builder()
                .cnt((int) ctfPage.getTotalElements())
                .list(ctfPage.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상위 5개 팀 정보 조회", description = """
    팀 이름, 팀원 닉네임
    """)
    @GetMapping("/ctf/team/all/rank_and_score/{leaguePk}")
    public ResponseEntity<CTFTeamDTO.Top5ListResponse> getTop5TeamList(
            @AuthenticationPrincipal User user,
            @PathVariable Long leaguePk)
    {
        CTFTeamDTO.Top5ListResponse response = ctfTeamService.getTop5TeamList(user, leaguePk);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "CTF팀 이름 중복 확인")
    @GetMapping("/ctf/team/name/{teamName}/exists")
    public ResponseEntity<Map> isExistTeamName(@PathVariable("teamName") String teamName) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", ctfTeamService.isExistTeamName(teamName));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Operation(summary = "ctf 팀 등수 매기기",
            description = """
            리그가 종료되어야 함.
            """
    )
    @PostMapping("/ctf/team/all/end/{leaguePk}")
    public ResponseEntity<CTFTeamDTO.TeamRankListResponse> getTeamRankAll(@AuthenticationPrincipal User user,
                                                                          @RequestBody @PathVariable Long leaguePk
    ) throws RuntimeException {

        CTFTeamDTO.TeamRankListResponse teamRankListResponse = ctfTeamService.getTeamRankList(leaguePk);

        return ResponseEntity.ok(teamRankListResponse);
    }

    @Operation(summary = "ctf 본인 팀 카테고리별 점수 확인", description = """
            본인 팀의 카테고리별 점수 확인"""
    )
    @GetMapping("/ctf/team/category/scores")
    public ResponseEntity<CTFTeamQuestionDTO.TeamScoreByCategoryListResponse> getMyTeamScoresByCategory(
            @AuthenticationPrincipal User user
    ) throws RuntimeException {

        CTFTeamQuestionDTO.TeamScoreByCategoryListResponse response =
                ctfTeamService.getMyTeamScoresByCategory(user);

        return ResponseEntity.ok(response);
    }



}
