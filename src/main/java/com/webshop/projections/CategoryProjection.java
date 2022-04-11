package com.webshop.projections;

import com.webshop.model.ParentCategory;

public interface CategoryProjection {
    String getName();
    Long getId();
    ParentCategory getParentCategory();
}
