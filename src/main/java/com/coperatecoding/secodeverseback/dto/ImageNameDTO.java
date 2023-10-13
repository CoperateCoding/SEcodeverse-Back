package com.coperatecoding.secodeverseback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageNameDTO {
    private String image_name;

    public ImageNameDTO(String imageName) {
        this.image_name = imageName;
    }
}
