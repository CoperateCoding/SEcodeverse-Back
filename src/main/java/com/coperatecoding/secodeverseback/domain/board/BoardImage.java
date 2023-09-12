package com.coperatecoding.secodeverseback.domain.board;

import com.coperatecoding.secodeverseback.domain.Image;
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
@Table(name = "board_image")
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_pk", referencedColumnName = "pk")
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_pk", referencedColumnName = "pk")
    private Image image;


}
