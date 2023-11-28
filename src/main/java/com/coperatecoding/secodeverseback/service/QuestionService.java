package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import com.coperatecoding.secodeverseback.dto.QuestionDTO;
import com.coperatecoding.secodeverseback.dto.QuestionSortType;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.LevelRepository;
import com.coperatecoding.secodeverseback.repository.QuestionCategoryRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final LevelRepository levelRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final UserRepository userRepository;

    public Question makeQuestion(User user, QuestionDTO.AddQuestionRequest addQuestionRequest) throws RuntimeException {

        QuestionCategory category = questionCategoryRepository.findById(addQuestionRequest.getCategoryPk())
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

        Level level = levelRepository.findById(addQuestionRequest.getLevelPk())
                .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));


        Question question = Question.makeQuestion(user, category, level, addQuestionRequest.getTitle(), addQuestionRequest.getIntro(), addQuestionRequest.getContent(), addQuestionRequest.getLimitations(), addQuestionRequest.getSource(), addQuestionRequest.getLanguage(), addQuestionRequest.getTestcaseDescription());

        question = questionRepository.save(question);
        return question;
    }


    public Question modifyQuestion(Long questionPk, QuestionDTO.AddQuestionRequest addQuestionRequest) throws RuntimeException {
        QuestionCategory category = questionCategoryRepository.findById(addQuestionRequest.getCategoryPk())
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

        Level level = levelRepository.findById(addQuestionRequest.getLevelPk())
                .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));
        Question question = questionRepository.findById(questionPk).orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
        question.editQuestion(category, level, addQuestionRequest.getTitle(), addQuestionRequest.getIntro(), addQuestionRequest.getContent(), addQuestionRequest.getLimitations(), addQuestionRequest.getSource(), addQuestionRequest.getLanguage(), addQuestionRequest.getTestcaseDescription());
        return question;
    }

    public Question findByPk(Long questionPk) {
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
        return question;
    }

    public QuestionDTO.AddQuestionResponse getDetailQuestion(Long questionPk) {
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));

        QuestionDTO.AddQuestionResponse questionResponse = QuestionDTO.AddQuestionResponse.builder()
                .pk(question.getPk())
                .content(question.getContent())
                .levelPk(question.getLevel().getPk())
                .title(question.getTitle())
                .language(question.getLanguage())
                .limitations(question.getLimitations())
                .testcaseDescription(question.getTestcaseDescription())
                .source(question.getSource())
                .build();

        return questionResponse;
    }
    private Question verifyWriterAndfindQuestion(User user, Long questionPk) {
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글이 없습니다"));

        //글 작성자거나, admin이 아니라면 수정 불가능
        if (user.getRoleType() != RoleType.ADMIN && question.getUser().getPk() != user.getPk())
            throw new ForbiddenException("권한이 없는 사용자");
        return question;
    }

    public List<QuestionDTO.questionPagingResponse> userPostQuestion(User user, int page, int size) {
        List<Question> allQuestions = questionRepository.findByUser(user);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Question> questions = questionRepository.findByUser(user, pageable);
        List<QuestionDTO.questionPagingResponse> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO.questionPagingRequest request = QuestionDTO.questionPagingRequest.questions(
                    allQuestions.size(),
                    question.getPk(),
                    user.getUsername(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getCategory().getPk()
            );

            QuestionDTO.questionPagingResponse response = getPagingQuestion(request);

            QuestionDTO.questionPagingResponse questionDTO = QuestionDTO.questionPagingResponse.builder()
                    .cnt(response.getCnt())
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public List<QuestionDTO.questionPagingResponse> userPagingQuestion(int cnt, List<QuestionDTO.SearchQuestionResponse> searchQuestion) {

        List<QuestionDTO.questionPagingResponse> questionDTOS = new ArrayList<>();
        for (QuestionDTO.SearchQuestionResponse question : searchQuestion) {
            QuestionDTO.questionPagingRequest request = QuestionDTO.questionPagingRequest.questions(
                    cnt,
                    question.getPk(),
                    question.getUserName(),
                    question.getLevelPk(),
                    question.getTitle(),
                    question.getCategoryPk()
            );

            QuestionDTO.questionPagingResponse response = getPagingQuestion(request);

            QuestionDTO.questionPagingResponse questionDTO = QuestionDTO.questionPagingResponse.builder()
                    .cnt(response.getCnt())
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public List<QuestionDTO.SearchQuestionResponse> getKeywordQuestion(String keyword) {
        List<Question> questions = questionRepository.findByTitleContaining(keyword);
        List<QuestionDTO.SearchQuestionResponse> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = question.getUser();
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    user.getUsername(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro(),
                    question.getCategory().getPk()
            );

            QuestionDTO.SearchQuestionResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public void deleteQuestion(User user,Long questionPK) {
        Question question =verifyWriterAndfindQuestion(user,questionPK);

        questionRepository.delete(question);
    }

    public Page<QuestionDTO.SearchQuestionResponse> getQuestion(int page, int pageSize, String q, QuestionSortType sort, List<Long> categoryPks, List<Long> levelPks) {
        Pageable pageable = makePageable(page, pageSize, q, sort, categoryPks, levelPks);
        List<Question> questions = questionRepository.findAll();
        List<QuestionDTO.SearchQuestionResponse> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = question.getUser();
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    user.getUsername(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro(),
                    question.getCategory().getPk()
            );

            QuestionDTO.SearchQuestionResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);

        }


        return new PageImpl<>(questionDTOS, pageable, questionDTOS.size()); // Return a Page object if necessary
    }

    private Pageable makePageable(Integer page, Integer pageSize, String q, QuestionSortType sortType, List<Long> categoryPks, List<Long> levelPks) {
        Sort sort;
        if (sortType == QuestionSortType.RECENT) {
            sort = Sort.by(Sort.Direction.DESC, "createAt");
        } else {
            sort = Sort.by(Sort.Direction.ASC, "createAt");
        }

        int pageNumber = page != null && page > 0 ? page - 1 : 0;
        int size = pageSize != null && pageSize > 1 ? pageSize : 10;

        return PageRequest.of(pageNumber, size, sort);
    }

//    public List<QuestionDTO.SearchQuestionResponse> getQuestion(){
//        List<Question>questions = questionRepository.findAll();
//        List<QuestionDTO.SearchQuestionResponse> questionDTOS= new ArrayList<>();
//        for(Question question:questions){
//            User user = question.getUser();
//            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
//                    question.getPk(),
//                    user.getUsername(),
//                    question.getLevel().getPk(),
//                    question.getTitle(),
//                    question.getIntro(),
//                    question.getCategory().getPk()
//            );
//
//            QuestionDTO.SearchQuestionResponse response = getQuestion(request);
//
//            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
//                    .pk(response.getPk())
//                    .userName(response.getUserName())
//                    .levelPk(response.getLevelPk())
//                    .title(response.getTitle())
//                    .intro(response.getIntro())
//                    .categoryPk(response.getCategoryPk())
//                    .build();
//            questionDTOS.add(questionDTO);
//
//        }
//        return questionDTOS;
//    }

    public QuestionDTO.SearchQuestionResponse getByPk(Question question) {
        QuestionDTO.SearchQuestionResponse response = QuestionDTO.SearchQuestionResponse.builder()
                .pk(question.getPk())
                .userName(question.getUser().getUsername())
                .levelPk(question.getLevel().getPk())
                .title(question.getTitle())
                .categoryPk(question.getCategory().getPk())
                .build();

        return response;
    }

    public QuestionDTO.SearchQuestionResponse getQuestion(QuestionDTO.SearchQuestionListRequest request) {
        QuestionDTO.SearchQuestionResponse response = QuestionDTO.SearchQuestionResponse.builder()
                .pk(request.getPk())
                .userName(request.getUserName())
                .levelPk(request.getLevelPk())
                .title(request.getTitle())
                .categoryPk(request.getCategoryPk())
                .build();

        return response;
    }

    public QuestionDTO.questionPagingResponse getPagingQuestion(QuestionDTO.questionPagingRequest request) {
        QuestionDTO.questionPagingResponse response = QuestionDTO.questionPagingResponse.builder()
                .cnt(request.getCnt())
                .pk(request.getPk())
                .userName(request.getUserName())
                .levelPk(request.getLevelPk())
                .title(request.getTitle())
                .categoryPk(request.getCategoryPk())
                .build();

        return response;
    }



    public List<QuestionDTO.SearchQuestionResponse> getRecentQuestion() {


        List<Question> questions = questionRepository.findAll();
        List<QuestionDTO.SearchQuestionResponse> resultQuestions = new ArrayList<>();
        List<QuestionDTO.SearchQuestionResponse> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = question.getUser();
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    user.getUsername(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro(),
                    question.getCategory().getPk()
            );

            QuestionDTO.SearchQuestionResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);


        }

        int numQuestions = questionDTOS.size();

        int startIndex = Math.max(numQuestions - 7, 0);
        for (int i = numQuestions - 1; i >= startIndex; i--) {
            resultQuestions.add(questionDTOS.get(i));
        }

        return resultQuestions;
    }

//    public Page<QuestionDTO.SearchQuestionResponse> getLevelQuestionList( Long levelPk){
//        Level level = levelRepository.findById(levelPk).orElseThrow(() -> new NotFoundException("해당하는 레벨 존재하지 않음"));;
//
//        List<Question>questions = questionRepository.findByLevel(level);
//        Page<QuestionDTO.SearchQuestionResponse> questionDTOS;
//        for(Question question:questions){
//            User user=question.getUser();
//            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
//                    question.getPk(),
//                    user.getUsername(),
//                    question.getLevel().getPk(),
//                    question.getTitle(),
//                    question.getIntro(),
//                    question.getCategory().getPk()
//            );
//
//            QuestionDTO.SearchQuestionResponse response = getQuestion(request);
//
//            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
//                    .pk(response.getPk())
//                    .userName(response.getUserName())
//                    .levelPk(response.getLevelPk())
//                    .title(response.getTitle())
//                    .intro(response.getIntro())
//                    .categoryPk(response.getCategoryPk())
//                    .build();
//            questionDTOS.add(questionDTO);
//        }
//        return questionDTOS;
//    }
//
//    public Page<QuestionDTO.SearchQuestionResponse> getCategoryQuestion(Long categoryPk){
//        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryPk).orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));;
//
//        List<Question> questions = questionRepository.findByCategory(questionCategory);
//
//        Page<QuestionDTO.SearchQuestionResponse> questionDTOS;
//        for(Question question:questions){
//            User user = question.getUser();
//            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
//                    question.getPk(),
//                    user.getUsername(),
//                    question.getLevel().getPk(),
//                    question.getTitle(),
//                    question.getIntro(),
//                    question.getCategory().getPk()
//            );
//
//            QuestionDTO.SearchQuestionResponse response = getQuestion(request);
//
//            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
//                    .pk(response.getPk())
//                    .userName(response.getUserName())
//                    .levelPk(response.getLevelPk())
//                    .title(response.getTitle())
//                    .intro(response.getIntro())
//                    .categoryPk(response.getCategoryPk())
//                    .build();
//            questionDTOS.add(questionDTO);
//
//        }
//        return questionDTOS;
//    }
//    public Page<QuestionDTO.SearchQuestionResponse> getMatchingQuestions(Long categoryPk, Long levelPk) {
//        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryPk)
//                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));
//
//        Level level = levelRepository.findById(levelPk)
//                .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));
//
//        Page<Question> questions = questionRepository.findByCategoryAndLevel(questionCategory, level);
//
//        Page<QuestionDTO.SearchQuestionResponse> questionDTOS;
//        for (Question question : questions) {
//            User user = question.getUser();
//            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
//                    question.getPk(),
//                    user.getUsername(),
//                    question.getLevel().getPk(),
//                    question.getTitle(),
//                    question.getIntro(),
//                    question.getCategory().getPk()
//            );
//
//            QuestionDTO.SearchQuestionResponse response = getQuestion(request);
//
//            QuestionDTO.SearchQuestionResponse questionDTO = QuestionDTO.SearchQuestionResponse.builder()
//                    .pk(response.getPk())
//                    .userName(response.getUserName())
//                    .levelPk(response.getLevelPk())
//                    .title(response.getTitle())
//                    .intro(response.getIntro())
//                    .categoryPk(response.getCategoryPk())
//                    .build();
//
//            questionDTOS.add(questionDTO);
//        }
//
//
//        return questionDTOS;
//    }


//    public Page<QuestionDTO.SearchQuestionResponse> getQuestionList(int page, int pageSize, String q, QuestionSortType sort, List<Long> categoryPks, List<Long> levelPks) {
//        Pageable pageable = makePageable(page, pageSize, q, sort, categoryPks, levelPks);
//        Page<Question> list;
//
//            if (q != null && !q.isEmpty()) {
//                // 검색어, 카테고리, 레벨
//                if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
//                    list = questionRepository.findByCategoryPksAndLevelPksAndTitleOrContentContaining(
//                            categoryPks, levelPks, q, pageable);
//                } // 검색어, 카테고리만
//                else if (categoryPks != null && !categoryPks.isEmpty()) {
//                    list = questionRepository.findByCategoryPksAndTitleOrContentContaining(categoryPks, q, pageable);
//                } //검색어, 레벨만
//                else if (levelPks != null && !levelPks.isEmpty()) {
//                    list = questionRepository.findByLevelPksAndTitleOrContentContaining(levelPks, q, pageable);
//                } // 검색어만
//                else {
//                    list = questionRepository.findByTitleOrContentContaining(q, pageable);
//                }
//            } else {
//                // 카테고리랑 레벨만
//                if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
//                    list = questionRepository.findByCategoryPksAndLevelPks(categoryPks, levelPks, pageable);
//                } else {
//                    list = questionRepository.findAll(pageable);
//                }
//            }
//
//
//            List<QuestionDTO.SearchQuestionResponse> searchList = list.getContent().stream()
//                .map(question -> QuestionDTO.SearchQuestionResponse.builder()
//                        .pk(question.getPk())
//                        .userName(question.getUser().getNickname())
//                        .categoryPk(question.getCategory().getPk())
//                        .title(question.getTitle())
//                        .levelPk(question.getLevel().getPk())
//                        .build()
//                )
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(searchList, pageable, list.getTotalElements());
//
//    }
//}


//    public Page<QuestionDTO.SearchQuestionResponse> getQuestionList(int page, int pageSize, String q, QuestionSortType sort, List<Long> categoryPks, List<Long> levelPks) {
//         List<Question> questions = questionRepository.findAll();
//
//        List<Question> filteredQuestions = questions.stream()
//                .filter(question -> {
//                    // 카테고리 필터링
//                    if (categoryPks != null && !categoryPks.isEmpty()) {
//                        if (!categoryPks.contains(question.getCategory().getPk())) {
//                            return false;
//                        }
//                    }
//                    // 레벨 필터링
//                    if (levelPks != null && !levelPks.isEmpty()) {
//                        if (!levelPks.contains(question.getLevel().getPk())) {
//                            return false;
//                        }
//                    }
//                    // 제목 또는 내용 필터링
//                    if (q != null && !q.isEmpty()) {
//                        String queryLowerCase = q.toLowerCase();
//                        String titleLowerCase = question.getTitle().toLowerCase();
//                        return titleLowerCase.contains(queryLowerCase);
//                    }
//                    return true;
//                })
//                .collect(Collectors.toList());
//
//        if (sort == QuestionSortType.RECENT) {
//            filteredQuestions.sort(Comparator.comparing(Question::getCreateAt).reversed());
//        }
//
//
//
//        int start = (page - 1) * pageSize;
//        int end = Math.min(start + pageSize, filteredQuestions.size());
//
//        start = Math.max(0, Math.min(start, filteredQuestions.size() - 1));
//        end = Math.min(end, filteredQuestions.size());
//
//
//        List<QuestionDTO.SearchQuestionResponse> searchList = filteredQuestions.subList(start, end).stream()
//                .map(question -> QuestionDTO.SearchQuestionResponse.builder()
//                        .pk(question.getPk())
//                        .userName(question.getUser().getNickname())
//                        .categoryPk(question.getCategory().getPk())
//                        .title(question.getTitle())
//                        .levelPk(question.getLevel().getPk())
//                        .build()
//                )
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(searchList, PageRequest.of(page, pageSize), filteredQuestions.size());
//    }
    public Page<QuestionDTO.SearchQuestionResponse> getQuestionList(int page, int pageSize, String q, QuestionSortType sort, List<Long> categoryPks, List<Long> levelPks) {
        Pageable pageable = makePageable(page, pageSize, sort);

        Specification<Question> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 카테고리 필터링
            if (categoryPks != null && !categoryPks.isEmpty()) {
                predicates.add(root.get("category").get("pk").in(categoryPks));
            }
            // 레벨 필터링
            if (levelPks != null && !levelPks.isEmpty()) {
                predicates.add(root.get("level").get("pk").in(levelPks));
            }
            // 제목 또는 내용 필터링
            if (q != null && !q.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + q.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Question> questions = questionRepository.findAll(spec, pageable);
        System.out.println("size1============="+questions.getTotalElements());


        List<QuestionDTO.SearchQuestionResponse> searchList = questions.stream()
                .map(question -> {
                    User user = question.getUser();
                    if (user == null) return null;
                    if (question.getCategory() == null) return null;
                    if (question.getLevel() == null) return null;

                    return QuestionDTO.SearchQuestionResponse.builder()
                            .pk(question.getPk())
                            .userName(user.getNickname())
                            .categoryPk(question.getCategory().getPk())
                            .title(question.getTitle())
                            .levelPk(question.getLevel().getPk())
                            .build();
                })
                .filter(Objects::nonNull)  // null인 요소 제거
                .collect(Collectors.toList());

        System.out.println("size============="+searchList.size());

        return new PageImpl<>(searchList, pageable, questions.getTotalElements());
    }

    private Pageable makePageable(int page, int pageSize, QuestionSortType sort) {
        page = page - 1;
        if (sort == QuestionSortType.RECENT) {
            return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createAt"));
        } else {
            return PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "createAt"));
        }
    }

    public void increaseExp(User user, Long questionPk) {
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 없습니다."));

        Level level = question.getLevel();
        Integer exp = level.getExp();
        user.addExp(exp);

        userRepository.save(user);

    }
}
