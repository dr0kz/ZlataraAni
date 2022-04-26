package com.webshop.service;

import com.webshop.model.Category;
import com.webshop.model.ParentCategory;
import com.webshop.model.exceptions.CategoryAlreadyExists;
import com.webshop.model.exceptions.CategoryNotFoundException;
import com.webshop.projections.CategoryProjection;
import com.webshop.repository.CategoryRepository;
import com.webshop.repository.ParentCategoryRepository;
import com.webshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ParentCategoryRepository parentCategoryRepository;
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, ParentCategoryRepository parentCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.parentCategoryRepository = parentCategoryRepository;
    }
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllWithProducts() {
        return this.categoryRepository.findAllWithProducts();
    }

    public List<CategoryProjection> findAllCategoriesProjection() {
        return this.categoryRepository.findAllByNameUrlNameAndParentCategoryProjection();

    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category deleteCategory(Long id) {
        Category category = this.findById(id);
        this.productRepository.deleteAll(this.productRepository.findAllByCategoryId(id));
        this.categoryRepository.delete(category);
        return category;
    }

    public Category createCategory(String name, Long parentCategoryId, String urlName) {
        ParentCategory parentCategory = this.parentCategoryRepository.findById(parentCategoryId).get();
        List<Category> optionalCategory =
                this.categoryRepository.findByNameAndParentCategoryOrUrlNameAndParentCategory(name, urlName, parentCategory.getId());
        if(optionalCategory.size()!=0){
            throw new CategoryAlreadyExists();
        }
        return categoryRepository.save(new Category(name, parentCategory, urlName));
    }

    public Category editCategory(Long id, String name,Long parentCategoryId, String urlName) {
        Category c = this.categoryRepository.findById(id).get();
        List<Category> optionalCategory ;
        ParentCategory parentCategory = this.parentCategoryRepository.findById(parentCategoryId).get();
        if(c.getParentCategory()!=parentCategory){
            optionalCategory =  this.categoryRepository.findDifferentByNameAndNotParentCategoryOrUrlNameAndNotParentCategory(id, name, urlName, parentCategoryId);
        }else{
            optionalCategory =  this.categoryRepository.findDifferentByNameAndParentCategoryOrUrlNameAndParentCategory(id, name, urlName, parentCategoryId);
        }
        if(optionalCategory.size()!=0){
            throw new CategoryAlreadyExists();
        }
        Category category = this.findById(id);
        category.setName(name);
        category.setUrlName(urlName);
        category.setParentCategory(parentCategory);
        categoryRepository.save(category);
        return category;
    }

    public List<Category> findAllCategoryNamesByParentCategory(Long parentCategoryId) {
        return this.categoryRepository.findAllByParentCategoryId(parentCategoryId);
    }

}
