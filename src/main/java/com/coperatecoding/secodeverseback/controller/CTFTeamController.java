package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.ctf.CTFLeagueDTO;
import com.coperatecoding.secodeverseback.dto.ctf.CTFTeamDTO;
import com.coperatecoding.secodeverseback.service.CTFLeagueService;
import com.coperatecoding.secodeverseback.service.CTFTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "CTF팀", description = "CTF 팀 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/ctf/team")
public class CTFTeamController {

    private final CTFTeamService ctfTeamService;

    @Operation(summary = "ctf 팀 등록")
    @PostMapping("/admin/ctf/team/post")
    public ResponseEntity makeCTFTeam(@AuthenticationPrincipal User user, @RequestBody @Valid CTFTeamDTO.AddRequest addRequest) {

        ctfTeamService.makeTeam(addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "ctf 팀 상세 조회")
    @GetMapping("/admin/ctf/team/{teamPk}")
    public ResponseEntity<CTFTeamDTO.DetailResponse> getCtfTeam(@PathVariable Long teamPk) throws NoSuchElementException {

        CTFTeamDTO.DetailResponse detailResponse = ctfTeamService.getDetailTeam(teamPk);

        return ResponseEntity.ok(detailResponse);
    }

}
