package com.webshop.web.controller;


import com.webshop.model.Product;
import com.webshop.repository.ProductRepository;
import com.webshop.service.CategoryService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequestMapping({"/products","/proizvodi"})
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;


    public ProductController(ProductService productService, ProductRepository productRepository, CategoryService categoryService) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public String addAndEdit(@RequestParam(required = false) Long id,
                             @RequestParam String name,
                             @RequestParam String code,
                             @RequestParam String description,
                             @RequestParam Integer stocks,
                             @RequestParam Integer price,
                             @RequestParam Integer discountPrice,
                             @RequestParam(defaultValue = "false") boolean isNew,
                             @RequestParam(defaultValue = "false") boolean isOnDiscount,
                             @RequestParam(defaultValue = "false") boolean isDealOfTheDay,
                             @RequestParam Long categoryId,
                             @RequestParam(required = false) MultipartFile initialPhoto,
                             @RequestParam(required = false) MultipartFile hoverPhoto,
                             @RequestParam(required = false) MultipartFile[] images
    ) {
        if (id != null) {
            this.productService.editProduct(id,name, code, description,
                    stocks, price, discountPrice,
                    isNew, isOnDiscount, isDealOfTheDay,
                    categoryId, initialPhoto, hoverPhoto, images);
        } else {
            this.productService.createProduct(name, code, description,
                    stocks, price, discountPrice,
                    isNew, isOnDiscount, isDealOfTheDay,
                    categoryId, initialPhoto, hoverPhoto, images);
        }
        return "redirect:/admin/products";
    }
    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model){
        Product product = this.productService.findById(id);
        List<Product> relatedProducts = this.productService.findRelatedProducts(id, product.getCategory().getId());
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts",relatedProducts);
        model.addAttribute("bodyContent","shop-single");
        return "master-template";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
