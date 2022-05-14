package com.webshop.projections;

import com.webshop.model.ParentCategory;

public interface CategoryProjection {
    String getName();
    String getUrlName();
    Long getId();
    ParentCategory getParentCategory();
}
