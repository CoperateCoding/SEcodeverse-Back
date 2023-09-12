package com.coperatecoding.secodeverseback.domain.board;

import com.coperatecoding.secodeverseback.domain.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    //사용자가 탈퇴했을 때 알수없음 처리를 위해 nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", referencedColumnName = "pk")
    private Users user;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @Column(name = "like_cnt")
    private Long likeCnt = 0L;

    @Column(name = "comment_cnt")
    private Long commentCnt = 0L;

    @NotNull
    private String title;

    @NotNull
    private String content;

    public String convertDate(LocalDateTime createAt) {
        String convertedDate = createAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
        return convertedDate;
    }



}
