package com.webshop.service;

import com.webshop.model.ParentCategory;

import java.util.List;

public interface ParentCategoryService {
    List<ParentCategory> findAll();
    ParentCategory findById(Long id);
    void createParentCategory(String name, String urlName);
    void editParentCategory(Long id, String name, String urlName);
    void deleteParentCategory(Long id);
}
