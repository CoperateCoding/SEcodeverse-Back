package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionImage;
import com.coperatecoding.secodeverseback.dto.QuestionImgDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.QuestionImageRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
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
public class QuestionImgService {
   private final QuestionRepository questionRepository;
   private final QuestionImageRepository questionImageRepository;
    public QuestionImage makeQuestionImg(Long questionPk, QuestionImgDTO.AddQuestionImgRequest addQuestionImgRequest ) throws RuntimeException{

        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));


        QuestionImage questionImage = QuestionImage.makeQuestionImg(addQuestionImgRequest.getImgUrl(),question);

            questionImage=questionImageRepository.save(questionImage);



        return questionImage;
    }
    public QuestionImage modifyQuestionImg(Long imgPk, QuestionImgDTO.AddQuestionImgRequest addQuestionImgRequest) throws RuntimeException{

        QuestionImage questionImage = questionImageRepository.findById(imgPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제 이미지가 존재하지 않음"));
        questionImage.modifyQuestionImg(addQuestionImgRequest.getImgUrl());
        return questionImage;

    }


    public List<QuestionImgDTO.SearchQuestionImgResponse> getQuestionImg(Long questionPk){
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));
        List<QuestionImage> questionImgs = questionImageRepository.findByQuestion(question);
        List<QuestionImgDTO.SearchQuestionImgResponse> questionImgDTOS = new ArrayList<>();
        for(QuestionImage questionImage: questionImgs){
            QuestionImgDTO.SearchQuestionImgRequest request = QuestionImgDTO.SearchQuestionImgRequest.questionImg(
                    questionImage.getPk(),
                    questionImage.getImgUrl()
            );
            QuestionImgDTO.SearchQuestionImgResponse response = getQuestionImg(request);
            QuestionImgDTO.SearchQuestionImgResponse questionImgDTO = QuestionImgDTO.SearchQuestionImgResponse.builder()
                    .pk(response.getPk())
                    .imgUrl(response.getImgUrl())
                    .build();
            questionImgDTOS.add(questionImgDTO);
        }
        return questionImgDTOS;
    }

    public QuestionImgDTO.SearchQuestionImgResponse getQuestionImg(QuestionImgDTO.SearchQuestionImgRequest request) {
        QuestionImgDTO.SearchQuestionImgResponse response = QuestionImgDTO.SearchQuestionImgResponse.builder()
                .pk(request.getPk())
                .imgUrl(request.getImgUrl())
                .build();

        return response;
    }
    public void delete(Long imgPk) throws RuntimeException {
        QuestionImage questionImage = questionImageRepository.findById(imgPk)
                .orElseThrow(() -> new NotFoundException("해당하는 이미지가 존재하지 않음"));

        questionImageRepository.delete(questionImage);
    }
}
