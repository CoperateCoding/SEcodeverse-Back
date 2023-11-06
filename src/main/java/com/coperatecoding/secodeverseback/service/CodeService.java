package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Code;
import com.coperatecoding.secodeverseback.domain.CodeStatus;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.CodeDTO;
import com.coperatecoding.secodeverseback.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CodeService {
    private final CodeRepository codeRepository;

    public CodeDTO.SearchCodeListResponse getCodes(CodeDTO.SearchCodeListRequest request){
        CodeDTO.SearchCodeListResponse response = CodeDTO.SearchCodeListResponse.builder()
                .pk(request.getPk())
                .codeStatus(request.getCodeStatus())
                .questionPk(request.getQuestionPk())
                .build();
        return response;
    }

    public List<CodeDTO.SearchCodeListResponse> getWrongCodes(User user){
        CodeStatus codeStatus = CodeStatus.FALSE;
        List<Code>codes = codeRepository.findByStatusAndUser(codeStatus,user);
        List<CodeDTO.SearchCodeListResponse>codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.SearchCodeListRequest request = CodeDTO.SearchCodeListRequest.Codes(
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.SearchCodeListResponse response = getCodes(request);
            CodeDTO.SearchCodeListResponse codeDTO = CodeDTO.SearchCodeListResponse.builder()
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }
        return codeDTOS;
    }

    public List<CodeDTO.SearchCodeListResponse> getCorrectQuestion(User user){
        CodeStatus codeStatus = CodeStatus.TRUE;
        List<Code>codes = codeRepository.findByStatusAndUser(codeStatus,user);
        List<CodeDTO.SearchCodeListResponse>codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.SearchCodeListRequest request = CodeDTO.SearchCodeListRequest.Codes(
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.SearchCodeListResponse response = getCodes(request);
            CodeDTO.SearchCodeListResponse codeDTO = CodeDTO.SearchCodeListResponse.builder()
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }
        return codeDTOS;
    }

    public List<CodeDTO.SearchCodeListResponse> getUserCodes(User user){
        List<Code>codes = codeRepository.findByUser(user);
        List<CodeDTO.SearchCodeListResponse>codeDTOS = new ArrayList<>();
        for(Code code: codes){
            CodeDTO.SearchCodeListRequest request = CodeDTO.SearchCodeListRequest.Codes(
                    code.getPk(),
                    code.getStatus(),
                    code.getQuestion().getPk()
            );
            CodeDTO.SearchCodeListResponse response = getCodes(request);
            CodeDTO.SearchCodeListResponse codeDTO = CodeDTO.SearchCodeListResponse.builder()
                    .pk(response.getPk())
                    .codeStatus(response.getCodeStatus())
                    .questionPk(response.getQuestionPk())
                    .build();
            codeDTOS.add(codeDTO);


        }
        return codeDTOS;
    }
}
