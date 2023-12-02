package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.*;
import com.coperatecoding.secodeverseback.dto.ctf.CTFQuestionDTO;
import com.coperatecoding.secodeverseback.dto.ctf.CTFQuestionSortType;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.*;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFQuestionService {
    private final CTFLeagueRepository ctfLeagueRepository;
    private final CTFQuestionRepository ctfQuestionRepository;
    private final CTFImageRepository ctfImageRepository;
    private final CTFCategoryRepository ctfCategoryRepository;
    private final CTFTeamRepository ctfTeamRepository;
    private final CTFTeamQuestionRepository ctfTeamQuestionRepository;

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

    private Pageable makePageable(CTFQuestionSortType sortType, Integer page, Integer pageSize) throws RuntimeException {

        Sort.Order defaultOrder = new Sort.Order(Sort.Direction.DESC, "score");
        Sort sort;
        if (sortType == CTFQuestionSortType.LOW) {
            sort = Sort.by(Sort.Direction.ASC, "score");
        }
        else {
            sort = Sort.by(defaultOrder);
        }

        int pageNumber = page != null && page > 0 ? page - 1 : 0;
        int size = pageSize != null && pageSize > 1 ? pageSize : 10;

        return PageRequest.of(pageNumber, size, sort);
    }


    public Page<CTFQuestionDTO.BriefResponse> getCTFQuestionAll(
            User user, Long categoryPk, int page, int pageSize, CTFQuestionSortType sort
    ) throws RuntimeException {

        Pageable pageable = makePageable(sort, page, pageSize);
        Page<CTFQuestion> list;

        if(categoryPk != null)
        {
            CTFCategory ctfCategory = ctfCategoryRepository.findById(categoryPk)
                    .orElseThrow(() -> new CategoryNotFoundException("해당하는 카테고리가 없습니다"));
            list = ctfQuestionRepository.findByCategory(ctfCategory, pageable);
        }
        else {
            list = ctfQuestionRepository.findAll(pageable);
        }

        CTFTeam ctfTeam = ctfTeamRepository.findByUsers(user)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 팀이 존재하지 않습니다."));

        List<CTFQuestionDTO.BriefResponse> briefResponseList = list.getContent().stream()
                .map(ctfQuestion -> {
                    // user의 팀이 해당 문제를 맞췄는지
                    boolean isExist = ctfTeamQuestionRepository.findByCtfQuestionAndCtfTeam(ctfQuestion, ctfTeam).isPresent();
                    return CTFQuestionDTO.BriefResponse.builder()
                                    .questionPk(ctfQuestion.getPk())
                                    .questionName(ctfQuestion.getName())
                                    .score(ctfQuestion.getScore())
                                    .categoryName(ctfQuestion.getCategory().getName())
                                    .isSolved(isExist)
                                    .build();
                        }
                ).collect(Collectors.toList());

        return new PageImpl<>(briefResponseList, pageable, list.getTotalElements());

    }

    public CTFQuestionDTO.DetailResponse getDetailCTFQuestion(Long ctfQuestionPk) {
        CTFQuestion ctfQuestion = ctfQuestionRepository.findById(ctfQuestionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 문제가 없습니다."));

        List<CTFImage> ctfImages = ctfImageRepository.findByCtfQuestion(ctfQuestion);
        String[] imgUrlList = new String[ctfImages.size()];
        for (int i = 0; i < ctfImages.size(); i++) {
            imgUrlList[i] = ctfImages.get(i).getImgUrl();
        }

        CTFQuestionDTO.DetailResponse response = CTFQuestionDTO.DetailResponse.builder()
                .categoryName(ctfQuestion.getCategory().getName())
                .ctfQuestionType(ctfQuestion.getType())
                .questionName(ctfQuestion.getName())
                .description(ctfQuestion.getDescription())
                .score(ctfQuestion.getScore())
                .imgUrlList(imgUrlList)
                .build();

        return response;

    }

    public boolean solveCTFQuestion(User user, Long ctfQuestionPk, CTFQuestionDTO.SolveRequest request
    ) throws RuntimeException {
        CTFQuestion ctfQuestion = ctfQuestionRepository.findById(ctfQuestionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 문제가 없습니다."));

        CTFTeam ctfTeam = ctfTeamRepository.findByUsers(user)
                .orElseThrow(() -> new NotFoundException("해당하는 ctf 팀이 없습니다."));

        Optional<CTFTeamQuestion> optionalCTFTeamQuestion  =
                ctfTeamQuestionRepository.findByCtfQuestionAndCtfTeam(ctfQuestion, ctfTeam);

        if (optionalCTFTeamQuestion.isPresent()) {
            throw new DuplicateRequestException("이미 해당 문제를 풀었습니다.");
        }

        boolean isCorrectAnswer = ctfQuestion.getAnswer().equals(request.getAnswer());
        int score = isCorrectAnswer && ctfQuestion.getScore() != null ? ctfQuestion.getScore() : 0;

        if (isCorrectAnswer) {
            // 정답이면 user의 ctfTeam의 점수가 증가
            ctfTeam.addScore(score);
            ctfTeamRepository.save(ctfTeam);
        }

        CTFTeamQuestion ctfTeamQuestion =
                CTFTeamQuestion.makeCTFTeamQuestion(ctfQuestion, ctfTeam, ctfQuestion.getCategory(),
                        score, isCorrectAnswer);
        ctfTeamQuestionRepository.save(ctfTeamQuestion);

        return isCorrectAnswer;
    }

    public void editRequest(Long ctfQuestionPk, CTFQuestionDTO.EditRequest request
    ) throws RuntimeException {

        CTFQuestion question = ctfQuestionRepository.findById(ctfQuestionPk)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 CTF 문제가 없습니다."));

        CTFCategory ctfCategory = ctfCategoryRepository.findById(request.getCategoryPk())
                .orElseThrow(() -> new CategoryNotFoundException("해당하는 CTF 카테고리를 찾을 수 없습니다."));

        question.edit(ctfCategory,
                request.getCtfQuestionType(),
                request.getName(),
                request.getScore(),
                request.getDescription(),
                request.getAnswer());

        List<CTFImage> ctfImages = ctfImageRepository.findByCtfQuestion(question);

        // 새로운 이미지 리스트가 있다면
        if (request.getImgUrlList() != null) {
            // 기존 이미지 리스트를 모두 삭제
            for (CTFImage ctfImage : ctfImages) {
                ctfImageRepository.delete(ctfImage);
            }

            // 새로운 이미지 리스트를 추가
            for (String newImgUrl : request.getImgUrlList()) {
                CTFImage image = CTFImage.builder()
                        .ctfQuestion(question)
                        .imgUrl(newImgUrl)
                        .build();
                ctfImageRepository.save(image);
            }
        } else {
            // 새 이미지 리스트가 null이면 모든 기존 이미지 삭제
            for (CTFImage ctfImage : ctfImages) {
                ctfImageRepository.delete(ctfImage);
            }
        }
    }

    public void deleteCTFQouestion(Long ctfQuestionPk) {
        CTFQuestion ctfQuestion = ctfQuestionRepository.findById(ctfQuestionPk)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 CTF 문제가 없습니다."));

        ctfQuestionRepository.delete(ctfQuestion);

    }
}
