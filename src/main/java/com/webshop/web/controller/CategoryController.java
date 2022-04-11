package com.webshop.web.controller;

import com.webshop.model.Product;
import com.webshop.model.exceptions.CategoryAlreadyExists;
import com.webshop.service.CategoryService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"/kategorii", "/categories"})
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/list")
    public String getCategories(Model model) {
        model.addAttribute("categories", this.categoryService.findAll());
        return "Category";
    }

    @PostMapping("/add")
    public String addOrEditCategory(@RequestParam(required = false) Long id,
                                    @RequestParam String categoryName,
                                    @RequestParam Long parentCategoryId,
                                    @RequestParam String urlName) {

        try {
            if (id != null) {
                this.categoryService.editCategory(id, categoryName, parentCategoryId, urlName);
            } else {
                this.categoryService.createCategory(categoryName, parentCategoryId, urlName);
            }
        } catch (CategoryAlreadyExists exception) {
            return "redirect:/admin/categories?error=" + exception.getMessage();
        }

        return "redirect:/admin/categories";
    }

    @GetMapping("/{parentCategoryUrl}")
    public String listAllByParentCategory(@PathVariable String parentCategoryUrl, Model model) {
        model.addAttribute("products", this.productService.findAllByParentCategoryUrl(parentCategoryUrl));
        model.addAttribute("bodyContent", "shop-catalog");
        return "master-template";
    }
    @GetMapping("/{parentCategoryUrl}/{categoryUrl}")
    public String listAllByParentCategoryAndCategory(@PathVariable String parentCategoryUrl, @PathVariable(required = false) String categoryUrl, Model model) {
        model.addAttribute("products", this.productService.findAllByCategoryUrlAndParentCategoryUrl(parentCategoryUrl, categoryUrl));
        model.addAttribute("bodyContent", "shop-catalog");
        return "master-template";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long id, Model model) {
        this.categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }


}
