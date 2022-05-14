package com.webshop.repository;

import com.webshop.model.Category;
import com.webshop.projections.CategoryProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"products"})
    @Query(value = "SELECT c FROM Category c")
    List<Category> findAllWithProducts();

    @Query(value = "SELECT c.name as name,c.urlName as urlName, c.id as id,c.parentCategory as parentCategory  FROM Category c")
    List<CategoryProjection> findAllByNameUrlNameAndParentCategoryProjection();

    List<Category> findAllByParentCategoryId(Long parentCategoryId);


    @Query("select c from Category c where c.name=:name and c.parentCategory.id = :parentCategory or " +
            "c.urlName=:urlName and c.parentCategory.id = :parentCategory")
    List<Category> findByNameAndParentCategoryOrUrlNameAndParentCategory(String name, String urlName, Long parentCategory);

    @Query("select c from Category c where c.id<>:id and (c.name=:name and c.parentCategory.id = :parentCategory or " +
            "c.urlName=:urlName and c.parentCategory.id = :parentCategory)")
    List<Category> findDifferentByNameAndParentCategoryOrUrlNameAndParentCategory(Long id, String name, String urlName, Long parentCategory);

    @Query("select c from Category c where c.id<>:id and (c.name=:name and c.parentCategory.id = :parentCategory or " +
            "c.urlName=:urlName and c.parentCategory.id = :parentCategory)")
    List<Category> findDifferentByNameAndNotParentCategoryOrUrlNameAndNotParentCategory(Long id, String name, String urlName, Long parentCategory);

}
