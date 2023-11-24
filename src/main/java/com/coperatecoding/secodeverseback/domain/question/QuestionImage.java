package com.coperatecoding.secodeverseback.domain.question;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "question_image")
public class QuestionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_pk", referencedColumnName = "pk")
    private Question question;

    @JoinColumn(name = "img_url")
    private String imgUrl;

    public static QuestionImage makeQuestionImg(String imgUrl, Question question){
        QuestionImage questionImage = new QuestionImage();
        questionImage.imgUrl=imgUrl;
        questionImage.question=question;
        return questionImage;
    }


    public void modifyQuestionImg(String imgUrl){
        this.imgUrl=imgUrl;
    }

}
