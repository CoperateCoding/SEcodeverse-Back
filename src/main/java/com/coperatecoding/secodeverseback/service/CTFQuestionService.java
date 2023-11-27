package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.domain.ctf.CTFImage;
import com.coperatecoding.secodeverseback.domain.ctf.CTFLeague;
import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestion;
import com.coperatecoding.secodeverseback.dto.ctf.CTFQuestionDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CTFCategoryRepository;
import com.coperatecoding.secodeverseback.repository.CTFImageRepository;
import com.coperatecoding.secodeverseback.repository.CTFLeagueRepository;
import com.coperatecoding.secodeverseback.repository.CTFQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFQuestionService {
    private final CTFLeagueRepository ctfLeagueRepository;
    private final CTFQuestionRepository ctfQuestionRepository;
    private final CTFImageRepository ctfImageRepository;
    private final CTFCategoryRepository ctfCategoryRepository;

    // ctf 문제 등록
    public void makeQuestion(CTFQuestionDTO.PostRequest request) {

        CTFLeague ctfLeague = ctfLeagueRepository.findById(request.getLeaguePk())
                .orElseThrow(() -> new NotFoundException("해당하는 리그가 존재하지 않습니다"));

        CTFCategory ctfCategory = ctfCategoryRepository.findById(request.getCategoryPk())
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않습니다."));

        CTFQuestion ctfQuestion = CTFQuestion.builder()
                .league(ctfLeague)
                .category(ctfCategory)
                .type(request.getCtfQuestionType())
                .name(request.getName())
                .score(request.getScore())
                .description(request.getDescription())
                .answer(request.getAnswer())
                .build();

        ctfQuestionRepository.save(ctfQuestion);

        if (request.getImgUrlList() != null) {
            for (String imgUrl : request.getImgUrlList()) {
                CTFImage ctfImage = CTFImage.builder()
                        .ctfQuestion(ctfQuestion)
                        .imgUrl(imgUrl)
                        .build();
                ctfImageRepository.save(ctfImage);
            }
        }
    }
}
