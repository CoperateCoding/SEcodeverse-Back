package com.coperatecoding.secodeverseback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageNameDTO {
    private String imageName;

    public ImageNameDTO(String imageName) {
        this.imageName = imageName;
    }
}
