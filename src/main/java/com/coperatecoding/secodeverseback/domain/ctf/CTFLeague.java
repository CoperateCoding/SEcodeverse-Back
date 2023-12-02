package com.coperatecoding.secodeverseback.domain.ctf;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Column(name = "open_time")
    private LocalDateTime openTime;

    @NotNull
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
    private CTFLeagueStatus status = CTFLeagueStatus.CLOSE;


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
    
    // 현재 리그 상태 확인
    public CTFLeagueStatus checkLeagueStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isEqual(this.openTime) || (now.isAfter(this.openTime) && now.isBefore(this.closeTime))) {
            this.status = CTFLeagueStatus.OPEN;
        } else {
            this.status = CTFLeagueStatus.CLOSE;
        }
        return status;
    }

    public void setLeagueStatus(CTFLeagueStatus leagueStatus)
    {
        this.status = leagueStatus;
    }

    public String convertDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public LocalDateTime convertStringtoDate(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(dateTime, formatter);
    }


    public void edit(String name, LocalDateTime openTime, LocalDateTime closeTime, int memberCnt, String notice, String description) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.memberCnt = memberCnt;
        this.notice = notice;
        this.description = description;
    }


}
