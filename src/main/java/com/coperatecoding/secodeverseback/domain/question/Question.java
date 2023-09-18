package com.coperatecoding.secodeverseback.domain.question;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    //사용자가 탈퇴했을 때 알수없음 처리를 위해 nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", referencedColumnName = "pk")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "category_pk", referencedColumnName = "pk")
    private QuestionCategory category;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<TestCase> testCases = new ArrayList<>();

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionStatus status = QuestionStatus.WAITING;

    @NotNull
    private String title; //제목
    
    private String intro; //한줄 설명


    private String reason; //(거부 또는 수정요청)사유

    @NotNull
    @Column(name = "report_cnt")
    private Long reportCnt = 0L;

    @NotNull
    private Integer level; //난이도

    @NotNull
    @Column(length = 99999)
    private String content; //내용

    @Column(length = 99999)
    private String limitations; //제한사항

    @Column(length = 99999)
    private String source; // 출처

    private String language;


    // 이거 너무 길어서 builder로 뺌.
    @Builder
    public Question(Users user, String title, String intro, String reason, int level, String content, String limitations, String source) {
        this.user = user;
        this.title = title;
        this.intro = intro;
        this.reason = reason;
        this.level = level;
        this.content = content;
        this.limitations = limitations;
        this.source = source;
    }

    public String convertDate(LocalDateTime createAt) {
        return createAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
    }


}
