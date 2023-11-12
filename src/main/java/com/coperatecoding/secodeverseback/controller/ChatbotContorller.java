package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.BoardDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Tag(name = "쳇봇", description = "쳇봇 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/chatbot")

public class ChatbotContorller {

    @GetMapping("")
    public ResponseEntity chatbot(@RequestParam String input) {


        // JSON 객체 생성 및 데이터 추가
        JSONObject json = new JSONObject();
        json.put("sentence", input);
        System.out.println(input);
        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 요청 바디에 JSON 데이터 설정
        HttpEntity<String> requestEntity = new HttpEntity<>(json.toString(), headers);

        // Flask 서버 URL 설정
        String url = "http://localhost:5000";

        // RestTemplate을 사용하여 POST 요청 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        // 응답 데이터 출력
        String response = responseEntity.getBody();
        System.out.println(response);
        return ResponseEntity.ok(response);
    }



}
