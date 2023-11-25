package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.dto.ctf.CTFLeagueDTO;
import com.coperatecoding.secodeverseback.service.CTFLeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CTF리그", description = "CTF 리그 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class CTFLeagueController {
    private final CTFLeagueService ctfLeagueService;

    @Operation(summary = "ctf 리그 등록")
    @PostMapping("/admin/make/ctf_league")
    public ResponseEntity makeCTFLeague(@AuthenticationPrincipal User user, @RequestBody @Valid CTFLeagueDTO.AddRequest addRequest) {

        ctfLeagueService.makeLeague(user, addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
