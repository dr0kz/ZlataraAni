package com.webshop.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCartProductsDto {

    public List<String> productIds;
    public List<String> productQuantities;

}
