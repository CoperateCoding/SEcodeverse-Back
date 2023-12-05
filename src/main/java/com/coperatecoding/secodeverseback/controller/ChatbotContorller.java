package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.Code;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.service.CodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.List;

@Tag(name = "챗봇", description = "챗봇 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/chatbot")
public class ChatbotContorller {
    private final CodeService codeService;
    @GetMapping("")
    public ResponseEntity chatbot(@RequestParam String input) {

        String url = "http://3.39.39.217:5000";
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
        String url = "http://3.39.39.217:5000/similarRecommend";
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

    String url = "http://3.39.39.217:5000";
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


    @PostMapping("/userRecommend")
    public ResponseEntity userRecommend (@AuthenticationPrincipal User user) {
        System.out.println("사용자 문제 추천 시작합니다.");
        List<Code>allCodes=codeService.userAllCodes(user);
        int allLevel =0;

        int allScore =0;
        int allMemory =0;

        for (int i=0; i<allCodes.size(); i++)
        {
            System.out.println(allCodes.get(i).getPk());
            allLevel += allCodes.get(i).getQuestion().getLevel().getPk();
            allScore += allCodes.get(i).getAccuracy();
            allMemory+=allCodes.get(i).getMemory();
        }
        int finalLevel = allLevel/allCodes.size();
        int finalScore = allScore/allCodes.size();
        int finalMemory =allMemory/allCodes.size();

        String url = "http://3.39.39.217:5000/userRecommend";
        String responseAnswer = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            JSONObject requestData = new JSONObject();
            requestData.put("level", finalLevel);
            requestData.put("tryCount",2);
            requestData.put("score",finalScore);
            requestData.put("memory",finalMemory);
            requestData.put("userSolvedQuestionPK","1,2,3");

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

        return ResponseEntity.ok(responseAnswer);}
    }
