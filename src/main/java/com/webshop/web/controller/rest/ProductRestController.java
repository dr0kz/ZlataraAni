package com.webshop.web.controller.rest;

import com.webshop.model.Product;
import com.webshop.model.dto.AdminProductDto;
import com.webshop.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public AdminProductDto getProduct(@PathVariable Long id) {
        Product p = this.productService.findById(id);
        return new AdminProductDto(p.getName(),p.getCode(),p.calculateDiscount(),p.getInitialPhotoEncoded());
    }

}
