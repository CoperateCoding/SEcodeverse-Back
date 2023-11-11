package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.dto.ImageNameDTO;
import com.coperatecoding.secodeverseback.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "S3Controller", description = "S3 관련 컨트롤러입니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "게시글 이미지 url 받아오기")

    @PostMapping("/presigned/board")
    public ResponseEntity getS3PresignedBoard(@RequestBody ImageNameDTO imageNameDTO) {
        String preSignedUrl = s3Service.getPreSignedBoard(imageNameDTO.getImageName());

        Map<String, String> map = new HashMap<>();
        map.put("presigned_url", preSignedUrl);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "문제 이미지 url 받아오기")

    @PostMapping("/presigned/board")
    public ResponseEntity getS3PresignedQuestion(@RequestBody ImageNameDTO imageNameDTO) {
        String preSignedUrl = s3Service.getPreSignedQuestion(imageNameDTO.getImageName());

        Map<String, String> map = new HashMap<>();
        map.put("presigned_url", preSignedUrl);

        return ResponseEntity.ok(map);
    }


}