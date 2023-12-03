package com.coperatecoding.secodeverseback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Tag(name = "챗봇", description = "챗봇 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/chatbot")
public class ChatbotContorller {

    @GetMapping("")
    public ResponseEntity chatbot(@RequestParam String input) {

        String url = "http://127.0.0.1:5000";
        String responseAnser="";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            JSONObject requestData = new JSONObject();
            requestData.put("sentence", input);

            OutputStream os = conn.getOutputStream();
            os.write(requestData.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // 응답 처리
                System.out.println("서버 응답: " + response.toString());
                responseAnser=response.toString();
            } else {
                System.out.println("서버 요청 실패. 응답 코드: " + responseCode);
            }

    } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(responseAnser);}

    @GetMapping("/similary")
    public ResponseEntity similar(
            @RequestParam int levelPk,
            @RequestParam int categoryPk
    ) {
        System.out.println(levelPk);
        System.out.println("유사문제 추천 시작합니다.");
        System.out.println(categoryPk);
        String url = "http://127.0.0.1:5000/similarRecommend";
        String responseAnswer = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            JSONObject requestData = new JSONObject();
            requestData.put("level", levelPk);
            requestData.put("category", categoryPk);

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(requestData.toString());
            osw.flush();
            osw.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                // 응답 처리
                System.out.println("서버 응답: " + response.toString());
                responseAnswer = response.toString();
            } else {
                System.out.println("서버 요청 실패. 응답 코드: " + responseCode);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(responseAnswer);
    }
//    @PostMapping("/codeReview")
//    public ResponseEntity codeReview(@RequestParam String code) {
//        System.out.println(code);
//
//        String url = "http://127.0.0.1:5000";
//        String responseAnswer = "";
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setDoOutput(true);
//
//            JSONObject requestData = new JSONObject();
//            requestData.put("code", code);
//            requestData.put("sentence","리뷰");
//
//            OutputStream os = conn.getOutputStream();
//            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
//            osw.write(requestData.toString());
//            osw.flush();
//            osw.close();
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    response.append(line);
//                }
//                br.close();
//
//                // 응답 처리
//                System.out.println("서버 응답: " + response.toString());
//                responseAnswer = response.toString();
//            } else {
//                System.out.println("서버 요청 실패. 응답 코드: " + responseCode);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.ok(responseAnswer);
//    }
@PostMapping("/codeReview")
public ResponseEntity codeReview(@RequestBody Map<String, String> requestBody) {
    String code = requestBody.get("code");
    System.out.println("입력받은 코드: " + code);

    String url = "http://127.0.0.1:5000";
    String responseAnswer = "";
    try {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        JSONObject requestData = new JSONObject();
        requestData.put("code", code);
        requestData.put("sentence","리뷰");

        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(requestData.toString());
        osw.flush();
        osw.close();

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // 응답 처리
            System.out.println("서버 응답: " + response.toString());
            responseAnswer = response.toString();
        } else {
            System.out.println("서버 요청 실패. 응답 코드: " + responseCode);
        }

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return ResponseEntity.ok(responseAnswer);
}
    }
