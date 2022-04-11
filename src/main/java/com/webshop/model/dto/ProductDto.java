package com.webshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    Long id = null;
    String name;
    String code;
    String description;
    Integer stocks;
    Integer price;
    boolean isNew = false;
    boolean isOnDiscount = false;
    boolean isDealOfTheDay = false;
    Long categoryId;
//    MultipartFile initialPhoto = null;
//    MultipartFile hoverPhoto = null;
//    MultipartFile[] images = null;
    List<String> deleteImages = null;
}
