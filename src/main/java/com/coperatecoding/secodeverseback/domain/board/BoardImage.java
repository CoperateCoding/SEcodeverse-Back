package com.coperatecoding.secodeverseback.domain.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board_image")
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_pk")
    private Board board;

    @JoinColumn(name = "img_url")
    private String imgUrl;


    public static BoardImage makeBoardImage(Board board, String imgUrl) {
        BoardImage boardImage = new BoardImage();
        boardImage.board = board;
        boardImage.imgUrl = imgUrl;

        return boardImage;
    }

    public void edit(String imgUrl) {
        this.imgUrl = Objects.nonNull(imgUrl) ? imgUrl : this.imgUrl;
    }

}
