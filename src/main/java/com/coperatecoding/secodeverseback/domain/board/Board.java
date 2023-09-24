package com.coperatecoding.secodeverseback.domain.board;

import com.coperatecoding.secodeverseback.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
    @JoinColumn(name = "user_pk")
    private User user;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now(); // LocalDate, LocalDateTime 사용 시 @Temporal 생략 가능

    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "category_pk")
    private BoardCategory category;

    @Column(name = "like_cnt")
    private Long likeCnt = 0L;

    @Column(name = "comment_cnt")
    private Long commentCnt = 0L;

    @NotNull
    @Column(length = 20)
    private String title;

    @NotNull
    @Column(length = 2000)
    private String content;

    public String convertDate(LocalDateTime createAt) {
        return createAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
    }

    public static Board makeBoard(User user, BoardCategory category, String title, String content) {
        Board board = new Board();
        board.user = user;
        board.category = category;
        board.title = title;
        board.content = content;
        return board;
    }

}
