package com.webshop.model.dto;

import com.webshop.model.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProductDto {
    String name;
    String code;
    Integer price;
    String image;
}
