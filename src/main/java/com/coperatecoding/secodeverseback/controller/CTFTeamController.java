package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamDTO;
import com.coperatecoding.secodeverseback.service.CTFTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "CTF팀", description = "CTF 팀 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/ctf/team")
public class CTFTeamController {

    private final CTFTeamService ctfTeamService;

    @Operation(summary = "ctf 팀 등록")
    @PostMapping("/post")
    public ResponseEntity makeCTFTeam(@AuthenticationPrincipal User user, @RequestBody @Valid CTFTeamDTO.AddRequest addRequest) {

        ctfTeamService.makeTeam(user, addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "ctf 팀 상세 조회",
            description = "관리자 or 유저의 팀이 있을때만 가능"
    )
    @GetMapping("/{teamPk}")
    public ResponseEntity<CTFTeamDTO.DetailResponse> getCtfTeam(@AuthenticationPrincipal User user, @PathVariable Long teamPk) throws NoSuchElementException {

        CTFTeamDTO.DetailResponse detailResponse = ctfTeamService.getDetailTeam(user, teamPk);

        return ResponseEntity.ok(detailResponse);
    }

//    @Operation(summary = "ctf 팀 참가")
//    @PostMapping("/post")
//    public ResponseEntity joinCTFTeam(@AuthenticationPrincipal User user, @RequestBody @Valid CTFTeamDTO.JoinRequest request) {
//
//        ctfTeamService.joinTeam(user, request);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @Operation(summary = "모든 팀 정보 조회", description = """
    팀 이름, 팀원 닉네임
    """)
    @GetMapping("/all/{leaguePk}")
    public ResponseEntity<CTFTeamDTO.SearchListResponse> getSearchTeamList(
            @AuthenticationPrincipal User user,
            @PathVariable Long leaguePk,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page는 1보다 커야합니다") int page,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1, message = "pageSize는 1보다 커야합니다") int pageSize) {

        Page<CTFTeamDTO.SearchResponse> ctfPage = ctfTeamService.getSearchTeam(user, leaguePk, page, pageSize);

        CTFTeamDTO.SearchListResponse response = CTFTeamDTO.SearchListResponse.builder()
                .cnt((int) ctfPage.getTotalElements())
                .list(ctfPage.getContent())
                .build();

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "상위 10개 팀 정보 조회", description = """
    팀 이름, 팀원 닉네임
    """)
    @GetMapping("/all/rank_and_score/{leaguePk}")
    public ResponseEntity<CTFTeamDTO.Top10ListResponse> getTop10TeamList(
            @AuthenticationPrincipal User user,
            @PathVariable Long leaguePk)
    {
        CTFTeamDTO.Top10ListResponse response = ctfTeamService.getTop10TeamList(user, leaguePk);

        return ResponseEntity.ok(response);
    }



}
