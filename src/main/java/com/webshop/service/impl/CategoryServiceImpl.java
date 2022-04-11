package com.webshop.service.impl;

import com.webshop.configuration.FileUploadUtil;
import com.webshop.model.Category;
import com.webshop.model.ParentCategory;
import com.webshop.model.exceptions.CategoryAlreadyExists;
import com.webshop.model.exceptions.CategoryNotFoundException;
import com.webshop.projections.CategoryProjection;
import com.webshop.repository.CategoryRepository;
import com.webshop.repository.ParentCategoryRepository;
import com.webshop.repository.ProductRepository;
import com.webshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ParentCategoryRepository parentCategoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ParentCategoryRepository parentCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.parentCategoryRepository = parentCategoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllWithProducts() {
        return this.categoryRepository.findAllWithProducts();
    }

    @Override
    public List<CategoryProjection> findAllCategoriesProjection() {
        return this.categoryRepository.findAllByNameUrlNameAndParentCategoryProjection();

    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category deleteCategory(Long id) {
        Category category = this.findById(id);
        this.productRepository.findAllByCategoryId(id).forEach(t -> {
            FileUploadUtil.deleteFile(t.getImagePath()+t.getInitialPhoto());
            FileUploadUtil.deleteFile(t.getImagePath()+t.getHoverPhoto());
            t.getImages().stream()
                    .map(k -> t.getImagePath()+k)
                    .forEach(FileUploadUtil::deleteFile);
            this.productRepository.delete(t);
        });
        this.categoryRepository.delete(category);
        return category;
    }

    @Override
    public Category createCategory(String name, Long parentCategoryId, String urlName) {
        ParentCategory parentCategory = this.parentCategoryRepository.findById(parentCategoryId).get();
        List<Category> optionalCategory =
                this.categoryRepository.findByNameAndParentCategoryOrUrlNameAndParentCategory(name, urlName, parentCategory.getId());
        if(optionalCategory.size()!=0){
            throw new CategoryAlreadyExists();
        }
        return categoryRepository.save(new Category(name, parentCategory, urlName));
    }

    @Override
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

    @Override
    public List<Category> findAllCategoryNamesByParentCategory(Long parentCategoryId) {
        return this.categoryRepository.findAllByParentCategoryId(parentCategoryId);
    }

}
