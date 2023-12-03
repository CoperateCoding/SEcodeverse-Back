package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.*;
import com.coperatecoding.secodeverseback.dto.ctf.CTFCategoryDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFCategoryService {

    private final CTFCategoryRepository ctfCategoryRepository;
    private final CTFLeagueRepository ctfLeagueRepository;
    private final CTFTeamRepository ctfTeamRepository;
    private final CTFQuestionRepository ctfQuestionRepository;
    private final CTFTeamQuestionRepository ctfTeamQuestionRepository;


    public List<CTFCategoryDTO> getCTFCategoryAll(User user, Long leaguePk) {
        List<CTFCategory> categories = ctfCategoryRepository.findAll();

        List<CTFCategoryDTO> categoryDTOs = categories.stream()
                .map(category -> CTFCategoryDTO.builder()
                        .pk(category.getPk())
                        .name(category.getName())
                        .isSolved(allQuestionsSolved(user, leaguePk, category.getPk()))
                        .build())
                .collect(Collectors.toList());


        //다 풀었는지 - 그 카테고리 문제 다 풀었는지 정보
        return categoryDTOs;
    }

    public Long getCategoryPk(String categoryName
    ) throws RuntimeException {

        CTFCategory ctfCategory = ctfCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException("해당 ctf 카테고리가 존재하지 않습니다."));

        return ctfCategory.getPk();
    }

    public Map<Long, Boolean> allQuestionsSolvedByCategory(User user, Long leaguePk){


        List<CTFCategory> categories = ctfCategoryRepository.findAll();

        Map<Long, Boolean> result = new HashMap<>();

        for (CTFCategory category : categories) {
            boolean allSolved = allQuestionsSolved(user, leaguePk, category.getPk());

            result.put(category.getPk(), allSolved);
        }

        return result;
    }

    public boolean allQuestionsSolved(User user, Long leaguePk, Long categoryPky) {

        CTFCategory ctfCategory = ctfCategoryRepository.findById(categoryPky)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 카테고리가 존재하지 않습니다."));

        CTFTeam ctfTeam = ctfTeamRepository.findByUsers(user)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 팀이 존재하지 않습니다."));

        CTFLeague ctfLeague = ctfLeagueRepository.findById(leaguePk)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 리그가 존재하지 않습니다."));

        boolean isTrue = true;

        List<CTFQuestion> questions = ctfQuestionRepository.findByLeagueAndCategory(ctfLeague, ctfCategory);
        System.out.println("question Size: " + questions.size());

        for (CTFQuestion question : questions) {
            // 해당 팀이 문제를 풀었는지 확인
            Optional<CTFTeamQuestion> ctfTeamQuestionOpt = ctfTeamQuestionRepository.findByCtfQuestionAndCtfTeam(question, ctfTeam);
            if (ctfTeamQuestionOpt.isEmpty()) {
                System.out.println("지금 이게 되는지???");
                isTrue = false;
            }
        }

        return isTrue;
    }

}
