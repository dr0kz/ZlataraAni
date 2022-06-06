package com.webshop.model.dto;

import com.webshop.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptedOrderDto {
    public Order order;
    public List<ProductQuantityDto> products;
}
