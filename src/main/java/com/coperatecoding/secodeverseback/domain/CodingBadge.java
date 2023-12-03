package com.coperatecoding.secodeverseback.domain;

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
@Table(name = "coding_badge")
public class CodingBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "img_url")
    private String imgUrl;

    public CodingBadge(Long pk){
        this.pk = pk;
    }

    public static CodingBadge makeCodingBadge(String name, String imgUrl) {
        CodingBadge codingBadge = new CodingBadge();
        codingBadge.name = name;
        codingBadge.imgUrl = imgUrl;
        return codingBadge;
    }
}
