package com.coperatecoding.secodeverseback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageNameDTO {
    private String folderName;
    private String imageName;

    public ImageNameDTO(String folderName, String imageName) {
        this.folderName = folderName;
        this.imageName = imageName;
    }
}