package com.coperatecoding.secodeverseback.domain.ctf;

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
@Table(name = "ctf_league")
public class CTFLeague {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @NotNull
    @CreationTimestamp
    @Column(name = "open_time")
    private LocalDateTime openTime;

    @NotNull
    @CreationTimestamp
    @Column(name = "close_time")
    private LocalDateTime closeTime;

    @NotNull
    @Column(name = "member_cnt")
    private Integer memberCnt;

    private String notice;

    @Column(name = "detail_description", length = 2000)
    private String detailDescription;

    @Enumerated(EnumType.STRING)
    private LeagueStatus status;

}
