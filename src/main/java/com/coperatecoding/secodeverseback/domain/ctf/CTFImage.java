package com.coperatecoding.secodeverseback.domain.ctf;

import com.coperatecoding.secodeverseback.domain.Image;
import com.coperatecoding.secodeverseback.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ctf_image")
public class CTFImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ctf_pk", referencedColumnName = "pk")
    private CTFQuestion ctfQuestion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_pk", referencedColumnName = "pk")
    private Image image;

}
