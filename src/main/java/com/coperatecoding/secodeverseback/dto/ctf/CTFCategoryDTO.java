package com.coperatecoding.secodeverseback.dto.ctf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CTFCategoryDTO {

    private Long pk;
    private String name;
    private boolean isSolved;
}
