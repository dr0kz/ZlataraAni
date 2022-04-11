package com.webshop.projections;

import com.webshop.model.Category;

public interface ProductProjection {
    Long getId();
    String getCode();
    String getName();
    Integer getPrice();
    Integer getStocks();
    Category getCategory();
    String getDescription();
}
