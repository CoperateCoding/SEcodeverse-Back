package com.coperatecoding.secodeverseback.domain.ctf;

import com.coperatecoding.secodeverseback.domain.Comment;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private int memberCnt;

    @Column(length = 2000)
    private String notice;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private CTFLeagueStatus status;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<CTFQuestion> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<CTFTeam> teamList = new ArrayList<>();


    public static CTFLeague makeCTFLeague(String name, LocalDateTime openTime, LocalDateTime closeTime, int memberCnt, String notice, String description) {
        CTFLeague ctfLeague = new CTFLeague();
        ctfLeague.name = name;
        ctfLeague.openTime = openTime;
        ctfLeague.closeTime = closeTime;
        ctfLeague.memberCnt = memberCnt;
        ctfLeague.notice = notice;
        ctfLeague.description = description;
        return ctfLeague;
    }


}
