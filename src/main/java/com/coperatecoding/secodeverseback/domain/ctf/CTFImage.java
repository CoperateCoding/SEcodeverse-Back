package com.coperatecoding.secodeverseback.domain.ctf;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "ctf_pk")
    private CTFQuestion ctfQuestion;

    @JoinColumn(name = "img_url")
    private String imgUrl;

    public static CTFImage makeCTFImage(CTFQuestion ctfQuestion, String imgUrl) {
        CTFImage ctfImage = new CTFImage();
        ctfImage.ctfQuestion = ctfQuestion;
        ctfImage.imgUrl = imgUrl;
        return ctfImage;
    }

    public void updateImgUrl(String s) {
        this.imgUrl = imgUrl;
    }
}
