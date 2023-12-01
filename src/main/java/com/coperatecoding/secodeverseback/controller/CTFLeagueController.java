package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.ctf.CTFLeagueDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.service.CTFLeagueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "CTF리그", description = "CTF 리그 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class CTFLeagueController {

    private final CTFLeagueService ctfLeagueService;

    @Operation(summary = "ctf 리그 등록")
    @PostMapping("/admin/ctf/league/post")
    public ResponseEntity makeCTFLeague(@AuthenticationPrincipal User user, @RequestBody CTFLeagueDTO.AddLeagueRequest addLeagueRequest
    ) throws RuntimeException {

        ctfLeagueService.makeLeague(addLeagueRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "현재 진행중인 리그 조회")
    @GetMapping("/ctf/league/current")
    public ResponseEntity<Long> getOngoingLeague() throws RuntimeException {
        Long leaguePk = ctfLeagueService.getOngoingLeague();
        return ResponseEntity.ok(leaguePk);
    }

    @Operation(summary = "ctf 리그 상세 조회")
    @GetMapping("/ctf/league/{leaguePk}")
    public ResponseEntity<CTFLeagueDTO.CTFLeagueDetailResponse> getCtfLeague(@PathVariable Long leaguePk) throws RuntimeException {

        CTFLeagueDTO.CTFLeagueDetailResponse league = ctfLeagueService.getDetailLeague(leaguePk);

        return ResponseEntity.ok(league);
    }

    @Operation(summary = "ctf 리그 전체 조회")
    @GetMapping("/admin/ctf/league/all")
    public ResponseEntity<CTFLeagueDTO.CTFListAllResponse> getCTFLeagueAll(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 2, message = "page 크기는 1보다 커야합니다") int pageSize,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page는 0보다 커야합니다") int page
            ) throws RuntimeException {

        Page<CTFLeagueDTO.BriefResponse> briefResponses = ctfLeagueService.getCTFLeagueAll(page, pageSize);


        CTFLeagueDTO.CTFListAllResponse response = CTFLeagueDTO.CTFListAllResponse.builder()
                .cnt((int) briefResponses.getTotalElements())
                .list(briefResponses.getContent())
                .build();

        return ResponseEntity.ok(response);


    }

    @Operation(summary = "ctf 리그 수정")
    @PatchMapping("/admin/ctf/league/{leaguePk}")
    public ResponseEntity editCTFLeague(@AuthenticationPrincipal User user, @PathVariable Long leaguePk, @RequestBody CTFLeagueDTO.EditRequest editRequest) {

        ctfLeagueService.editCTFLeague(leaguePk, editRequest);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "ctf 리그 현재 상태 받아오기")
    @GetMapping("/ctf/league/{leaguePk}/status")
    public ResponseEntity<CTFLeagueDTO.StatusResponse> getCTFLeagueStatus(@PathVariable Long leaguePk) {

        CTFLeagueDTO.StatusResponse  ctfLeagueStatus = ctfLeagueService.getCTFLeagueStatus(leaguePk);

        return ResponseEntity.ok(ctfLeagueStatus);
    }

    @Operation(summary = "ctf 리그 삭제")
    @DeleteMapping("/admin/ctf/league/{leaguePk}")
    public ResponseEntity deleteCTFLeague(@AuthenticationPrincipal User user, @RequestBody @PathVariable Long leaguePk) {
        try {
            ctfLeagueService.deleteCTFLeague(leaguePk);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }





}
