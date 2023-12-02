package com.coperatecoding.secodeverseback.domain.question;

import com.coperatecoding.secodeverseback.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
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
    @JoinColumn(name = "user_pk")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_pk")
    private QuestionCategory category;

    @ManyToOne
    @JoinColumn(name = "level_pk")
    private Level level;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<TestCase> testCaseList = new ArrayList<>();

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt=LocalDateTime.now() ;

    @NotNull
    private String title; //제목
    
    private String intro; //한줄 설명

    @NotNull
    @Column(length = 99999)
    private String content; //내용

    @Column(length = 99999)
    private String limitations; //제한사항

    @Column(length = 99999)
    private String source; // 출처

    private String language; // 언어

    @Column(length = 99999, name = "testcase_description")
    private String testcaseDescription;

    // 이거 너무 길어서 builder로 뺌.
    @Builder
    public Question(User user, String title, String intro, String content, String limitations, String source, String language) {
        this.user = user;
        this.title = title;
        this.intro = intro;
        this.content = content;
        this.limitations = limitations;
        this.source = source;
        this.language = language;
    }

    public String convertDate(LocalDateTime createAt) {
        return createAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
    }




    public static Question  makeQuestion(User user,QuestionCategory category,Level level,String title, String intro, String content, String limitations, String source, String language, String testcaseDescription){
        Question question = new Question();

        question.level=level;
        question.testcaseDescription=testcaseDescription;
        question.user = user;
        question.title = title;
        question.intro = intro;
        question.content = content;
        question.limitations = limitations;
        question.source = source;
        question.language = language;
        question.category=category;
    return question;

}

    public void  editQuestion(QuestionCategory category, Level level, String title, String intro, String content, String limitations, String source, String language, String testcaseDescription){
       this.category=category;
       this.level=level;
       this.title=title;
       this.intro=intro;
       this.content=content;
       this.limitations=limitations;
       this.source=source;
       this.language=language;
       this.testcaseDescription=testcaseDescription;
        this.updateAt=LocalDateTime.now();
    }


}
