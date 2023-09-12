package com.coperatecoding.secodeverseback.domain.question;

import com.coperatecoding.secodeverseback.domain.Image;
import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestion;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Question ctfQuestion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_pk", referencedColumnName = "pk")
    private Image image;

}
