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

import java.util.ArrayList;
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
        CodeDTO.SearchCodeListResponse response = CodeDTO.SearchCodeListResponse.builder()
                .pk(request.getPk())
                .codeStatus(request.getCodeStatus())
                .questionPk(request.getQuestionPk())
                .build();
        return response;
    }
    public CodeDTO.PageableCodeListResponse getPagingCodes(CodeDTO.PageableCodeListRequest request){
        CodeDTO.PageableCodeListResponse response = CodeDTO.PageableCodeListResponse.builder()
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
            CodeDTO.PageableCodeListRequest request = CodeDTO.PageableCodeListRequest.Codes(
                    allCodes.size(),
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.PageableCodeListResponse response = getPagingCodes(request);
            CodeDTO.PageableCodeListResponse codeDTO = CodeDTO.PageableCodeListResponse.builder()
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
        List<CodeDTO.PageableCodeListResponse>codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.PageableCodeListRequest request = CodeDTO.PageableCodeListRequest.Codes(
                    allCodes.size(),
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.PageableCodeListResponse response = getPagingCodes(request);
            CodeDTO.PageableCodeListResponse codeDTO = CodeDTO.PageableCodeListResponse.builder()
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
            CodeDTO.PageableCodeListRequest request = CodeDTO.PageableCodeListRequest.Codes(
                    codes.size(),
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.PageableCodeListResponse response = getPagingCodes(request);
            CodeDTO.PageableCodeListResponse codeDTO = CodeDTO.PageableCodeListResponse.builder()
                    .cnt(codes.size())
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }
        return codeDTOS;
    }

    public CodeDTO.MyTrueQuestionResponseList getCalendar(User user, int year, int month) {

        // 사용자가 특정 년도, 월에 맞춘 코드들을 가지고 옴.
        List<Code> filteredCodes = codeRepository.findByStatusAndUserAndYearAndMonth(CodeStatus.TRUE, user, year, month);

        // 날짜별로 문제를 맞춘 횟수를 계산함.
        Map<Integer, Long> dailyCount = filteredCodes.stream()
                .collect(Collectors.groupingBy(code -> code.getCreateAt().getDayOfMonth(), Collectors.counting()));

        List<CodeDTO.MyTrueQuestionResponse> responseList = dailyCount.entrySet().stream()
                .map(entry -> new CodeDTO.MyTrueQuestionResponse(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());

        CodeDTO.MyTrueQuestionResponseList response = CodeDTO.MyTrueQuestionResponseList.builder()
                .cnt(responseList.size())
                .list(responseList)
                .build();

        return response;
    }


}
