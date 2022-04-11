package com.webshop.service;


import com.webshop.model.Product;
import com.webshop.model.dto.ProductDto;
import com.webshop.projections.ProductProjection;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<Product> findAllWithImages();

    Product findById(Long id);

    List<ProductProjection> findAllValidProductProjection();

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByIsOnDiscount();

    List<Product> findAllDealOfTheDayProducts();

    Product createProduct(String name,
                          String code,
                          String description,
                          Integer stocks,
                          Integer price,
                          Integer discountPrice,
                          boolean isNew,
                          boolean isOnDiscount,
                          boolean isDealOfTheDay,
                          Long categoryId,
                          MultipartFile initialPhoto,
                          MultipartFile hoverPhoto,
                          MultipartFile[] images);


    void deleteProduct(Long id);

    Product editProduct(Long id,
                        String name,
                        String code,
                        String description,
                        Integer stocks,
                        Integer price,
                        Integer discountPrice,
                        boolean isNew,
                        boolean isOnDiscount,
                        boolean isDealOfTheDay,
                        Long categoryId,
                        MultipartFile initialPhoto,
                        MultipartFile hoverPhoto,
                        MultipartFile[] images);

    List<Product> findAllByCategoryUrlAndParentCategoryUrl(String parentCategoryUrl, String categoryUrl);

    List<Product> findRelatedProducts(Long productId, Long categoryId);

    List<Product> findAllByParentCategoryUrl(String parentCategoryUrl);
}
