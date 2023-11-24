package com.coperatecoding.secodeverseback.dto.board;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BriefBoardInfoDTO {
    private Long boardPk;
    private DateTime createAt;
    private Long likeCnt;
    private Long commentCnt;
    private String title;
    private String imgUrl;
}
