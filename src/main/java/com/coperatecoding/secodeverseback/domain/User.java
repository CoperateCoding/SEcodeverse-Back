package com.coperatecoding.secodeverseback.domain;

import com.coperatecoding.secodeverseback.domain.board.Likes;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_pk")
    private CTFTeam team;

//    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "badge_pk")
    private CodingBadge badge;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType = RoleType.USER;

    @NotNull
    @Column(unique = true, length =12)
    private String id;

    //    @Column(length = 20)
    @NotNull
    private String pw;

    @NotNull
    private String name;

    //NotNull 대신 Column에 nullable=false 해도 됨.
    @NotNull
    @Column(unique = true, length = 8)
    private String nickname;
    
    private Integer exp; // 경험치

    @OneToMany(mappedBy = "user")
    private List<Likes> likes = new ArrayList<>();

    private boolean isAccountNonLocked = true;

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public User() {
        this.badge = new CodingBadge();
    }

    public static User makeUsers(String id, String pw, String name, String nickname) {
        User user = new User();
        user.id = id;
        user.pw = pw;
        user.name = name;
        user.nickname = nickname;
        return user;
    }

    @Override
    public String getPassword() { return this.pw; }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override //만료여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //잠긴 사용자
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override //자격증명 만료
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //활성화, 비활성화 여부
    public boolean isEnabled() { //활성화상태인가? - 휴면상태 관리
        return true;
    }


    public void updateInfo(String nickname) {
        this.nickname = (nickname != null)? nickname : this.nickname;
    }

    public void updatePw(String pw) {
        this.pw = (pw != null)? pw : this.pw;
    }

    public void lock() {
        this.isAccountNonLocked = false;
    }

    public void unlock() {
        this.isAccountNonLocked = true;
    }

    public String convertDate(LocalDateTime createAt) {
        return createAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm:ss"));
    }

    public void setBadge(CodingBadge codingBadge){
        this.badge = codingBadge;
    }

    public void setTeam(CTFTeam team) { this.team = team; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roleType == RoleType.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        return authorities;
    }



}
