package com.coperatecoding.secodeverseback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponseDTO {
    private int cnt;
    private List<BriefBoardInfoDTO> list;
}
