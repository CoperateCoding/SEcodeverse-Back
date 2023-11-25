package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.ctf.CTFLeagueStatus;
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
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "CTF리그", description = "CTF 리그 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class CTFLeagueController {

    private final CTFLeagueService ctfLeagueService;

    @Operation(summary = "ctf 리그 등록")
    @PostMapping("/admin/ctf/league/post")
    public ResponseEntity makeCTFLeague(@RequestBody CTFLeagueDTO.PostRequest addRequest) throws RuntimeException {

        ctfLeagueService.makeLeague(addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "ctf 리그 상세 조회")
    @GetMapping("/ctf/league/{leaguePk}")
    public ResponseEntity<CTFLeagueDTO.DetailResponse> getCtfLeague(@PathVariable Long leaguePk) throws NoSuchElementException {

        CTFLeagueDTO.DetailResponse league = ctfLeagueService.getDetailLeague(leaguePk);

        return ResponseEntity.ok(league);
    }

    @Operation(summary = "ctf 리그 전체 조회")
    @DeleteMapping("/admin/ctf/league/all")
    public ResponseEntity<CTFLeagueDTO.AllListResponse> getCTFLeagueAll(
            @RequestParam(required = false, defaultValue = "10") @Min(value = 2, message = "page 크기는 1보다 커야합니다") int pageSize,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page는 0보다 커야합니다") int page
            ) throws RuntimeException {

        Page<CTFLeagueDTO.BriefResponse> briefResponses = ctfLeagueService.getCTFLeagueAll(page, pageSize);


        CTFLeagueDTO.AllListResponse response = CTFLeagueDTO.AllListResponse.builder()
                .cnt((int) briefResponses.getTotalElements())
                .list(briefResponses.getContent())
                .build();

        return ResponseEntity.ok(response);


    }

    @Operation(summary = "ctf 리그 수정")
    @PatchMapping("/admin/ctf/league/{leaguePk}")
    public ResponseEntity editCTFLeague(@PathVariable Long leaguePk, @RequestBody CTFLeagueDTO.EditRequest editRequest) {

        ctfLeagueService.editCTFLeague(leaguePk, editRequest);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "ctf 리그 현재 상태 받아오기")
    @PatchMapping("/ctf/league/{leaguePk}/status")
    public ResponseEntity getCTFLeagueStatus(@RequestBody @PathVariable Long leaguePk) {

        CTFLeagueStatus ctfLeagueStatus = ctfLeagueService.getCTFLeagueStatus(leaguePk);

        return ResponseEntity.ok(ctfLeagueStatus);
    }

    @Operation(summary = "ctf 리그 삭제")
    @DeleteMapping("/admin/ctf/league/{leaguePk}")
    public ResponseEntity deleteCTFLeague(@RequestBody @PathVariable Long leaguePk) {
        try {
            ctfLeagueService.deleteCTFLeague(leaguePk);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }




}
