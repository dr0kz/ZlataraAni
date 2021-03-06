package com.webshop.service;

import com.webshop.model.ParentCategory;
import com.webshop.model.exceptions.ParentCategoryAlreadyExists;
import com.webshop.model.exceptions.ParentCategoryNotFound;
import com.webshop.repository.ParentCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParentCategoryService {

    private final ParentCategoryRepository parentCategoryRepository;

    public ParentCategoryService(ParentCategoryRepository parentCategoryRepository) {
        this.parentCategoryRepository = parentCategoryRepository;
    }

    public List<ParentCategory> findAll() {
        return this.parentCategoryRepository.findAll();
    }

    public ParentCategory findById(Long id) {
        return this.parentCategoryRepository.findById(id)
                .orElseThrow(() -> new ParentCategoryNotFound(id));
    }

    public void createParentCategory(String name, String urlName) {
        Optional<ParentCategory> parentCategory = this.parentCategoryRepository.findByNameOrUrl(name, urlName);
        if (parentCategory.isEmpty()) {
            this.parentCategoryRepository.save(new ParentCategory(name, urlName));
        } else {
            throw new ParentCategoryAlreadyExists();
        }
    }

    public void editParentCategory(Long id, String name, String urlName) {
        ParentCategory parentCategory = this.parentCategoryRepository.findById(id)
                .orElseThrow(() -> new ParentCategoryNotFound(id));
        Optional<ParentCategory> alreadyExist = this.parentCategoryRepository
                .findByIdNotAndNameOrIdNotAndUrl(id, name, id, urlName);
        if (alreadyExist.isEmpty()) {
            parentCategory.setName(name);
            parentCategory.setUrl(urlName);
            this.parentCategoryRepository.save(parentCategory);
        } else {
            throw new ParentCategoryAlreadyExists();
        }
    }

    public void deleteParentCategory(Long id) {
        this.parentCategoryRepository.deleteById(id);
    }
}
