package com.coperatecoding.secodeverseback.domain.question;

import com.coperatecoding.secodeverseback.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "question_comment")
public class QuestionComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_pk", referencedColumnName = "pk")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_pk", referencedColumnName = "pk")
    private Question question;

    private String content;

}
