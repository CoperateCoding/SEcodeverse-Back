package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import com.coperatecoding.secodeverseback.dto.BoardSortType;
import com.coperatecoding.secodeverseback.dto.QuestionDTO;
import com.coperatecoding.secodeverseback.dto.QuestionSortType;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.LevelRepository;
import com.coperatecoding.secodeverseback.repository.QuestionCategoryRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final LevelRepository levelRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    public Question makeQuestion(User user,  QuestionDTO.AddQuestionRequest addQuestionRequest) throws RuntimeException{

        QuestionCategory category = questionCategoryRepository.findById(addQuestionRequest.getCategoryPk())
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

        Level level = levelRepository.findById(addQuestionRequest.getLevelPk())
                .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));



        Question question = Question.makeQuestion(user, category, level, addQuestionRequest.getTitle(), addQuestionRequest.getIntro(), addQuestionRequest.getContent(), addQuestionRequest.getLimitations(), addQuestionRequest.getSource(), addQuestionRequest.getLanguage(), addQuestionRequest.getTestcaseDescription());

        question = questionRepository.save(question);
        return question;
    }


        public Question modifyQuestion(Long questionPk, QuestionDTO.AddQuestionRequest addQuestionRequest) throws RuntimeException{
            QuestionCategory category = questionCategoryRepository.findById(addQuestionRequest.getCategoryPk())
                    .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

            Level level = levelRepository.findById(addQuestionRequest.getLevelPk())
                    .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));
            Question question = questionRepository.findById(questionPk) .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
            question.editQuestion(category,level,addQuestionRequest.getTitle(),addQuestionRequest.getIntro(),addQuestionRequest.getContent(),addQuestionRequest.getLimitations(),addQuestionRequest.getSource(),addQuestionRequest.getLanguage(),addQuestionRequest.getTestcaseDescription());
            return question;
    }
    public Question findByPk(Long questionPk){
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
        return question;
    }

    public Question getDetailQuestion(Long questionPk){
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
        return question;
    }

    public List<QuestionDTO.questionPagingResponse> userPostQuestion(User user,int page, int size){
        List<Question> allQuestions = questionRepository.findByUser(user);
        Pageable pageable =  PageRequest.of(page-1, size);
        Page<Question> questions = questionRepository.findByUser(user,pageable);
        List<QuestionDTO.questionPagingResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            QuestionDTO.questionPagingRequest request = QuestionDTO.questionPagingRequest.questions(
                    allQuestions.size(),
                    question.getPk(),
                    user.getUsername(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro(),
                    question.getCategory().getPk()
            );

            QuestionDTO.questionPagingResponse response = getPagingQuestion(request);

            QuestionDTO.questionPagingResponse questionDTO = QuestionDTO.questionPagingResponse.builder()
                    .cnt(response.getCnt())
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }
    public List<QuestionDTO.questionPagingResponse> userPagingQuestion(int cnt,List<QuestionDTO.SearchQuestionResponse> searchQuestion){

        List<QuestionDTO.questionPagingResponse> questionDTOS= new ArrayList<>();
        for(QuestionDTO.SearchQuestionResponse question:searchQuestion){
            QuestionDTO.questionPagingRequest request = QuestionDTO.questionPagingRequest.questions(
                    cnt,
                    question.getPk(),
                    question.getUserName(),
                    question.getLevelPk(),
                    question.getTitle(),
                    question.getIntro(),
                    question.getCategoryPk()
            );

            QuestionDTO.questionPagingResponse response = getPagingQuestion(request);

            QuestionDTO.questionPagingResponse questionDTO = QuestionDTO.questionPagingResponse.builder()
                    .cnt(response.getCnt())
                    .pk(response.getPk())
                    .userName(response.getUserName())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .categoryPk(response.getCategoryPk())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public List<QuestionDTO.SearchQuestionResponse> getKeywordQuestion(String keyword){
        List<Question> questions=questionRepository.findByTitleContaining(keyword);
        List<QuestionDTO.SearchQuestionResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
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
        return questionDTOS;
    }

    public void deleteQuestion(Long questionPK){
        Question question = questionRepository.findById(questionPK).orElseThrow(() -> new NotFoundException("해당하는 댓글이 존재하지 않음"));
        questionRepository.delete(question);
    }
    public List<QuestionDTO.SearchQuestionResponse> getQuestion(){
        List<Question>questions = questionRepository.findAll();
        List<QuestionDTO.SearchQuestionResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
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
        return questionDTOS;
    }

    public QuestionDTO.SearchQuestionResponse getByPk(Question question){
        QuestionDTO.SearchQuestionResponse response = QuestionDTO.SearchQuestionResponse.builder()
                .pk(question.getPk())
                .userName(question.getUser().getUsername())
                .levelPk(question.getLevel().getPk())
                .title(question.getTitle())
                .intro(question.getIntro())
                .categoryPk(question.getCategory().getPk())
                .build();

        return response;
    }

    public QuestionDTO.SearchQuestionResponse getQuestion(QuestionDTO.SearchQuestionListRequest request){
        QuestionDTO.SearchQuestionResponse response = QuestionDTO.SearchQuestionResponse.builder()
                .pk(request.getPk())
                .userName(request.getUserName())
                .levelPk(request.getLevelPk())
                .title(request.getTitle())
                .intro(request.getIntro())
                .categoryPk(request.getCategoryPk())
                .build();

        return response;
    }

    public QuestionDTO.questionPagingResponse getPagingQuestion(QuestionDTO.questionPagingRequest request){
        QuestionDTO.questionPagingResponse response = QuestionDTO.questionPagingResponse.builder()
                .cnt(request.getCnt())
                .pk(request.getPk())
                .userName(request.getUserName())
                .levelPk(request.getLevelPk())
                .title(request.getTitle())
                .intro(request.getIntro())
                .categoryPk(request.getCategoryPk())
                .build();

        return response;
    }

    public List<QuestionDTO.SearchQuestionResponse> getRecentQuestion(){


        List<Question>questions = questionRepository.findAll();
        List<QuestionDTO.SearchQuestionResponse>resultQuestions=new ArrayList<>();
        List<QuestionDTO.SearchQuestionResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            User user=question.getUser();
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

        int numQuestions = questionDTOS.size();
        for (int i = numQuestions - 1; i >= 0; i--) {
            resultQuestions.add(questionDTOS.get(i));
        }

        return resultQuestions;
    }

    public List<QuestionDTO.SearchQuestionResponse> getLevelQuestionList( Long levelPk){
        Level level = levelRepository.findById(levelPk).orElseThrow(() -> new NotFoundException("해당하는 레벨 존재하지 않음"));;

        List<Question>questions = questionRepository.findByLevel(level);
        List<QuestionDTO.SearchQuestionResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            User user=question.getUser();
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
        return questionDTOS;
    }

    public List<QuestionDTO.SearchQuestionResponse> getCategoryQuestion(Long categoryPk){
        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryPk).orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));;

        List<Question> questions = questionRepository.findByCategory(questionCategory);

        List<QuestionDTO.SearchQuestionResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
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
        return questionDTOS;
    }
    public List<QuestionDTO.SearchQuestionResponse> getMatchingQuestions(Long categoryPk, Long levelPk) {
        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryPk)
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

        Level level = levelRepository.findById(levelPk)
                .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));

        List<Question> questions = questionRepository.findByCategoryAndLevel(questionCategory, level);

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


        return questionDTOS;
    }


}
