package com.webshop.repository;

import com.webshop.model.ParentCategory;
import com.webshop.model.Product;
import com.webshop.projections.ProductProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"images"})
    @Query(value = "SELECT p FROM Product p")
    List<Product> findAllWithImages();

    Optional<Product> findByCode(String code);

    @Query(value = "select p.id as id, p.code as code, p.name as name, " +
            "p.price as price, p.stocks as stocks, p.category as category, " +
            "p.description as description from Product p")
    List<ProductProjection> findAllValidProductProjection();

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByCategoryUrlNameAndCategoryParentCategory(String categoryName, ParentCategory parentCategory);

    List<Product> findTop10ByIdNotAndCategoryId(Long productId, Long categoryId);

    @Transactional
    List<Product> findAllByCategoryParentCategoryUrl(String parentCategoryUrl);

    List<Product> findAllByIsDealOfTheDay(Boolean isDealOfTheDay);

    @Transactional
    List<Product> findAllByIsOnDiscount(Boolean isOnDiscount);

}
