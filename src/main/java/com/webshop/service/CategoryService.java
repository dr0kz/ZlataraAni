package com.webshop.service;


import com.webshop.model.Category;
import com.webshop.projections.CategoryProjection;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    List<Category> findAllWithProducts();

    List<CategoryProjection> findAllCategoriesProjection();

    Category findById(Long id);

    Category createCategory(String name, Long parentCategoryId, String urlName);

    Category deleteCategory(Long id);

    Category editCategory(Long id, String name,Long parentCategoryId,String urlName);

    List<Category> findAllCategoryNamesByParentCategory(Long parentCategoryId);


}
