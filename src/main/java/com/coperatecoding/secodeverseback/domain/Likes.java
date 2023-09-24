package com.coperatecoding.secodeverseback.domain;

import com.coperatecoding.secodeverseback.domain.board.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    //user가 탈퇴하면 알수없음으로 처리, like 개수는 유지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_pk")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_pk")
    private Board board;

    public static Likes makeLikes(User user, Board board) {
        Likes likes = new Likes();
        likes.user = user;
        likes.board = board;
        return likes;
    }

}
