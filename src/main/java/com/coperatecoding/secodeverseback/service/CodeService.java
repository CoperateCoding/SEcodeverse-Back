package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Code;
import com.coperatecoding.secodeverseback.domain.CodeStatus;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.question.CodeDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CodeRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CodeService {

    private final CodeRepository codeRepository;
    private final QuestionRepository questionRepository;


    public CodeDTO.SearchCodeListResponse getCodes(CodeDTO.SearchCodeListRequest request){
        CodeDTO.SearchCodeListResponse response =
                CodeDTO.SearchCodeListResponse.builder()
                .pk(request.getPk())
                .codeStatus(request.getCodeStatus())
                .questionPk(request.getQuestionPk())
                .build();
        return response;
    }
    public CodeDTO.PageableCodeListResponse getPagingCodes(CodeDTO.PageableCodeListRequest request){
        CodeDTO.PageableCodeListResponse response =
                CodeDTO.PageableCodeListResponse.builder()
                .cnt(request.getCnt())
                .pk(request.getPk())
                .codeStatus(request.getCodeStatus())
                .questionPk(request.getQuestionPk())
                .build();
        return response;
    }

    public Code makeCode(User user, Long questionPk, CodeDTO.AddCodeRequest addCodeRequest) throws RuntimeException{
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 없습니다."));

       Code code = Code.makeCode(user, question, addCodeRequest.getContent(), addCodeRequest.getCompileTime(),
               addCodeRequest.getMemory(), addCodeRequest.getAccuracy());

        System.out.println("=================" + code.getCompileTime());

        return codeRepository.save(code);
    }

    public List<CodeDTO.PageableCodeListResponse> getWrongCodes(User user, int page, int size){

        CodeStatus codeStatus = CodeStatus.FALSE;

        List<Code>allCodes = codeRepository.findByStatusAndUser(codeStatus,user);
        Pageable pageable =  PageRequest.of(page-1, size);
        Page<Code>codes = codeRepository.findByStatusAndUser(codeStatus,user,pageable);

        List<CodeDTO.PageableCodeListResponse>codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.PageableCodeListRequest request =
                    CodeDTO.PageableCodeListRequest.Codes(
                    allCodes.size(),
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.PageableCodeListResponse response = getPagingCodes(request);
            CodeDTO.PageableCodeListResponse codeDTO =
                    CodeDTO.PageableCodeListResponse.builder()
                    .cnt(response.getCnt())
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }

        return codeDTOS;
    }


    public List<CodeDTO.PageableCodeListResponse> getUserCodes(User user,int page,int pageSize){
        List<Code>allCodes = codeRepository.findByUser(user);
        Pageable pageable =  PageRequest.of(page-1, pageSize);
        Page<Code>codes = codeRepository.findByUser(user,pageable);
        List<CodeDTO.PageableCodeListResponse> codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.PageableCodeListRequest request =
                    CodeDTO.PageableCodeListRequest.Codes(
                    allCodes.size(),
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.PageableCodeListResponse response = getPagingCodes(request);
            CodeDTO.PageableCodeListResponse codeDTO =
                    CodeDTO.PageableCodeListResponse.builder()
                    .cnt(response.getCnt())
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }
        return codeDTOS;
    }

    public List<CodeDTO.PageableCodeListResponse> getPagingCodes(User user){
        List<Code> codes = codeRepository.findByUser(user);
        List<CodeDTO.PageableCodeListResponse> codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.PageableCodeListRequest request =
                    CodeDTO.PageableCodeListRequest.Codes(
                    codes.size(),
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.PageableCodeListResponse response =
                    getPagingCodes(request);
            CodeDTO.PageableCodeListResponse codeDTO =
                    CodeDTO.PageableCodeListResponse.builder()
                    .cnt(codes.size())
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }
        return codeDTOS;
    }

    public CodeDTO.MyTrueQuestionResponseList getCalendar(User user) {

        // 사용자 맞춘 코드 들고 옴.
        List<Code> correctCodeList = codeRepository.findByStatusAndUser(CodeStatus.TRUE, user);

        // 날짜별로 그룹화
        Map<LocalDate, Long> dateCountMap = correctCodeList.stream()
                .collect(Collectors.groupingBy(
                        code -> code.getCreateAt().toLocalDate(),
                        Collectors.counting()
                ));

        List<CodeDTO.MyTrueQuestionResponse> responseList = new ArrayList<>();
        for (Map.Entry<LocalDate, Long> entry : dateCountMap.entrySet()) {
            CodeDTO.MyTrueQuestionResponse trueQuestionResponse =
                    CodeDTO.MyTrueQuestionResponse.builder()
                            .time(entry.getKey().atStartOfDay()) // LocalDate를 LocalDateTime으로 변환
                            .cnt(entry.getValue().intValue()) // 맞춘 코드의 개수
                            .build();
            responseList.add(trueQuestionResponse);
        }

        // 리스트를 날짜 순으로 정렬
        responseList.sort(Comparator.comparing(CodeDTO.MyTrueQuestionResponse::getTime));

        CodeDTO.MyTrueQuestionResponseList response =
                CodeDTO.MyTrueQuestionResponseList.builder()
                        .cnt(responseList.size())
                        .list(responseList)
                        .build();

        return response;
    }


}
