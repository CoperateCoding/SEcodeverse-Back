package com.coperatecoding.secodeverseback.domain.board;

import com.coperatecoding.secodeverseback.domain.Users;
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
    private BoardStatus status = BoardStatus.WAITING;

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
    @Lob
    @Column(length = 99999)
    private String content;

    public String convertDate(LocalDateTime createAt) {
        String convertedDate = createAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
        return convertedDate;
    }

    public static Board makeBoard(Users user, BoardCategory category, String title, String content) {
        Board board = new Board();
        board.user = user;
        board.category = category;
        board.title = title;
        board.content = content;
        return board;
    }

    // only admin
    public static Board makeNotice(Users user, String title, String content) {
        Board board = new Board();
        board.user = user;
        board.status = BoardStatus.APPROVED;
        board.category = BoardCategory.NOTICE;
        board.title = title;
        board.content = content;
        return board;
    }

    //얘는 너무 길어서 builder 사용해도 되긴 함
//    @Builder
//    public Board(Users user, BoardCategory category, String title, String content) {
//        this.user = user;
//        this.category = category;
//        this.title = title;
//        this.content = content;
//    }
//
//    // 관리자 - 공지사항
//    @Builder
//    public Board(Users user, BoardStatus status, BoardCategory category, String title, String content) {
//        this.user = user;
//        this.status = APPROVED;
//        this.category = category;
//        this.title = title;
//        this.content = content;
//    }




}
