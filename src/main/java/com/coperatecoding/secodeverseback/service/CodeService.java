package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Code;
import com.coperatecoding.secodeverseback.domain.CodeStatus;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.CodeDTO;
import com.coperatecoding.secodeverseback.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public CodeDTO.PageableCodeListResponse getPagingCodes(CodeDTO.PageableCodeListRequest request){
        CodeDTO.PageableCodeListResponse response = CodeDTO.PageableCodeListResponse.builder()
                .cnt(request.getCnt())
                .pk(request.getPk())
                .codeStatus(request.getCodeStatus())
                .questionPk(request.getQuestionPk())
                .build();
        return response;
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
        List<Code>codes = codeRepository.findByUser(user);
        List<CodeDTO.PageableCodeListResponse>codeDTOS = new ArrayList<>();
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
}
