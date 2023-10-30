package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Comment;
import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import com.coperatecoding.secodeverseback.dto.CommentDTO;
import com.coperatecoding.secodeverseback.dto.QuestionDTO;
import com.coperatecoding.secodeverseback.dto.QuestionandTestCaseDTO;
import com.coperatecoding.secodeverseback.dto.TestCaseDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.LevelRepository;
import com.coperatecoding.secodeverseback.repository.QuestionCategoryRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Question getDetailQuestion(Long questionPk){
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
        return question;
    }

    public List<QuestionDTO.SearchQuestionListResponse> userPostQuestion(User user){
        List<Question> questions = questionRepository.findByUser(user);
        List<QuestionDTO.SearchQuestionListResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro()
            );

            QuestionDTO.SearchQuestionListResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionListResponse questionDTO = QuestionDTO.SearchQuestionListResponse.builder()
                    .pk(response.getPk())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public List<QuestionDTO.SearchQuestionListResponse> getKeywordQuestion(String keyword){
        List<Question> questions=questionRepository.findByTitleContaining(keyword);
        List<QuestionDTO.SearchQuestionListResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro()
            );

            QuestionDTO.SearchQuestionListResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionListResponse questionDTO = QuestionDTO.SearchQuestionListResponse.builder()
                    .pk(response.getPk())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public void deleteQuestion(Long questionPK){
        Question question = questionRepository.findById(questionPK).orElseThrow(() -> new NotFoundException("해당하는 댓글이 존재하지 않음"));
        questionRepository.delete(question);
    }
    public List<QuestionDTO.SearchQuestionListResponse> getQuestion(){
        List<Question>questions = questionRepository.findAll();
        List<QuestionDTO.SearchQuestionListResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro()
            );

            QuestionDTO.SearchQuestionListResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionListResponse questionDTO = QuestionDTO.SearchQuestionListResponse.builder()
                    .pk(response.getPk())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .build();
            questionDTOS.add(questionDTO);

        }
        return questionDTOS;
    }

    public QuestionDTO.SearchQuestionListResponse getQuestion(QuestionDTO.SearchQuestionListRequest request){
        QuestionDTO.SearchQuestionListResponse response = QuestionDTO.SearchQuestionListResponse.builder()
                .pk(request.getPk())
                .levelPk(request.getLevelPk())
                .title(request.getTitle())
                .intro(request.getIntro())
                .build();

        return response;
    }


    public List<QuestionDTO.SearchQuestionListResponse> getLevelQuestionList(boolean isSort,Long levelPk){
        Level level = levelRepository.findById(levelPk).orElseThrow(() -> new NotFoundException("해당하는 레벨 존재하지 않음"));;

        List<Question>questions = questionRepository.findByLevel(level);
        List<QuestionDTO.SearchQuestionListResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro()
            );

            QuestionDTO.SearchQuestionListResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionListResponse questionDTO = QuestionDTO.SearchQuestionListResponse.builder()
                    .pk(response.getPk())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .build();
            questionDTOS.add(questionDTO);
            if(isSort==true){
                Collections.sort(questionDTOS, (q1, q2) -> q1.getLevelPk().compareTo(q2.getLevelPk()));
            }

        }
        return questionDTOS;
    }

    public List<QuestionDTO.SearchQuestionListResponse> getCategoryQuestion(boolean isSort, Long categoryPk){
        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryPk).orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));;

        List<Question> questions = questionRepository.findByCategory(questionCategory);

        List<QuestionDTO.SearchQuestionListResponse> questionDTOS= new ArrayList<>();
        for(Question question:questions){
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro()
            );

            QuestionDTO.SearchQuestionListResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionListResponse questionDTO = QuestionDTO.SearchQuestionListResponse.builder()
                    .pk(response.getPk())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .build();
            questionDTOS.add(questionDTO);
            if(isSort==true){
                Collections.sort(questionDTOS, (q1, q2) -> q1.getLevelPk().compareTo(q2.getLevelPk()));
            }
        }
        return questionDTOS;
    }
    public List<QuestionDTO.SearchQuestionListResponse> getMatchingQuestions( boolean isSort,Long categoryPk, Long levelPk) {
        QuestionCategory questionCategory = questionCategoryRepository.findById(categoryPk)
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

        Level level = levelRepository.findById(levelPk)
                .orElseThrow(() -> new NotFoundException("해당하는 레벨이 존재하지 않음"));

        List<Question> questions = questionRepository.findByCategoryAndLevel(questionCategory, level);

        List<QuestionDTO.SearchQuestionListResponse> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO.SearchQuestionListRequest request = QuestionDTO.SearchQuestionListRequest.questions(
                    question.getPk(),
                    question.getLevel().getPk(),
                    question.getTitle(),
                    question.getIntro()
            );

            QuestionDTO.SearchQuestionListResponse response = getQuestion(request);

            QuestionDTO.SearchQuestionListResponse questionDTO = QuestionDTO.SearchQuestionListResponse.builder()
                    .pk(response.getPk())
                    .levelPk(response.getLevelPk())
                    .title(response.getTitle())
                    .intro(response.getIntro())
                    .build();

            questionDTOS.add(questionDTO);
        }
        if(isSort==true){
            Collections.sort(questionDTOS, (q1, q2) -> q1.getLevelPk().compareTo(q2.getLevelPk()));
        }

        return questionDTOS;
    }


}
