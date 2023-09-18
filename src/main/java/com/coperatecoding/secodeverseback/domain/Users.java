package com.coperatecoding.secodeverseback.domain;

import com.coperatecoding.secodeverseback.domain.ctf.Team;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_pk")
    private Team team;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="badge_pk", referencedColumnName = "pk")
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
    @Column(unique = true)
    private String id;

    @NotNull
    private String pw;

    //NotNull 대신 Column에 nullable=false 해도 됨.
    @Column(unique = true, nullable = false, length = 10)
    private String nickname;

    @NotNull
    @Column(name = "phone_num")
    private String phoneNum;

    @NotNull
    private String major;

    @NotNull
    private String email;

    @Column(name = "email_check")
    private boolean emailCheck = false;
    
    private Integer exp; // 경험치

    private boolean isAccountNonLocked = true;

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public static Users makeUsers(String id, String pw) {
        Users user = new Users();
        user.id = id;
        user.pw = pw;
        return user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void updateInfo(String nickname) {
        this.nickname = (nickname != null)? nickname : this.nickname;
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

}
