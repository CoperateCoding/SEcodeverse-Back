package com.coperatecoding.secodeverseback.domain.ctf;

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
@Table(name = "ctf_category")
public class CTFCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String name;

    public static CTFCategory makeCategory(String name) {
        CTFCategory newCategory = new CTFCategory();
        newCategory.name  = name;
        return newCategory;
    }

    public void updateName(String name) { this.name = name; }
}
