package com.coperatecoding.secodeverseback.domain;

import com.coperatecoding.secodeverseback.config.LocalTimeAttributeConverter;
import com.coperatecoding.secodeverseback.domain.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "code")
public class                                                                                                                                                                                                            Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    //사용자가 탈퇴했을 때 알수없음 처리를 위해 nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_pk")
    private Question question;

    @NotNull
    @Column(length = 99999)
    private String content;
    @Convert(converter = LocalTimeAttributeConverter.class)
    @Column(columnDefinition = "TIME(6)")
    private LocalTime compileTime;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @NotNull
    private Long memory;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CodeStatus status = CodeStatus.WAITING;

    private Double accuracy;

    @OneToMany(mappedBy = "code", cascade = CascadeType.ALL)
    private List<CodeLanguage> languageList = new ArrayList<>();

    public static Code makeCode(User user, Question question, String content, String compileTime, Long memory , Double accuracy){
        Code code = new Code();
        code.user= user;
        code.question = question;
        code.content = content;
        code.compileTime = LocalTime.parse(compileTime);
        //code.compileTime = compileTime;
        code.memory = memory;
        code.accuracy = accuracy;
        return code;

    }

}
