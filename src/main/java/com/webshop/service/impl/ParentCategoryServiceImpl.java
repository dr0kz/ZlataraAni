package com.webshop.service.impl;

import com.webshop.model.ParentCategory;
import com.webshop.model.exceptions.ParentCategoryAlreadyExists;
import com.webshop.model.exceptions.ParentCategoryNotFound;
import com.webshop.repository.ParentCategoryRepository;
import com.webshop.service.ParentCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentCategoryServiceImpl implements ParentCategoryService {

    private final ParentCategoryRepository parentCategoryRepository;

    public ParentCategoryServiceImpl(ParentCategoryRepository parentCategoryRepository) {
        this.parentCategoryRepository = parentCategoryRepository;
    }

    @Override
    public List<ParentCategory> findAll() {
        return this.parentCategoryRepository.findAll();
    }

    @Override
    public ParentCategory findById(Long id) {
        return this.parentCategoryRepository.findById(id)
                .orElseThrow(() -> new ParentCategoryNotFound(id));
    }

    @Override
    public void createParentCategory(String name, String urlName) {
        Optional<ParentCategory> parentCategory = this.parentCategoryRepository.findByNameOrUrl(name, urlName);
        if(parentCategory.isEmpty()){
            this.parentCategoryRepository.save(new ParentCategory(name, urlName));
        }else{
            throw new ParentCategoryAlreadyExists();
        }
    }

    @Override
    public void editParentCategory(Long id, String name, String urlName) {
        ParentCategory parentCategory = this.parentCategoryRepository.findById(id)
                .orElseThrow(() -> new ParentCategoryNotFound(id));
        parentCategory.setName(name);
        parentCategory.setUrl(urlName);
        this.parentCategoryRepository.save(parentCategory);
    }

    @Override
    public void deleteParentCategory(Long id) {
        this.parentCategoryRepository.deleteById(id);
    }
}
