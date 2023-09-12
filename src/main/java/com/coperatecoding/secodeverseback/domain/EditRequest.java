package com.coperatecoding.secodeverseback.domain;

import com.coperatecoding.secodeverseback.domain.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "edit_request")
public class EditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    //user가 탈퇴하면 알수없음 처리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_pk", referencedColumnName = "pk")
    private Users user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_pk", referencedColumnName = "pk")
    private Question question;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @NotNull
    @Column(length = 999999999)
    private String content; // 사유

    public static EditRequest makeEditRequest(Users user, Question question, String content) {
        EditRequest editRequest = new EditRequest();
        editRequest.user = user;
        editRequest.question = question;
        editRequest.content = content;

        return editRequest;
    }


}