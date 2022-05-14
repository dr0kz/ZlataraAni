package com.webshop.repository;

import com.webshop.model.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParentCategoryRepository extends JpaRepository<ParentCategory, Long> {
    Optional<ParentCategory> findByNameOrUrl(String name, String url);
    Optional<ParentCategory> findByIdNotAndNameOrIdNotAndUrl(Long id, String name,Long sameId, String url);
}
