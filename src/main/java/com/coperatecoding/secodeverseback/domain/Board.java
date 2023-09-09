package com.coperatecoding.secodeverseback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

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

    @Column(name = "view_cmt")
    private Long viewCnt = 0L;

    @NotNull
    private String title;

    @NotNull
    private String content;

}
