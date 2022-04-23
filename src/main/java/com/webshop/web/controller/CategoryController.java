package com.webshop.web.controller;

import com.webshop.model.Product;
import com.webshop.model.dto.ProductsTotalPages;
import com.webshop.model.exceptions.CategoryAlreadyExists;
import com.webshop.service.CategoryService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/{categoryUrl}/{parentCategoryUrl}")
    public String listAllByParentCategoryAndCategory(@PathVariable String parentCategoryUrl,
                                                     @PathVariable String categoryUrl,
                                                     @RequestParam(required = false, defaultValue = "bez-sortiranje") String sortiranje,
                                                     @RequestParam(required = false) String cenaOdDo,
                                                     @RequestParam(required = false,defaultValue = "0") Integer strana,
                                                     @RequestParam(required = false,defaultValue = "12") Integer produktiPoStrana,
                                                     Model model) {
        List<Product> productList;
        Integer maxPrice = this.productService.findBiggestProductPriceByCategoryUrlAndParentCategoryUrl(categoryUrl, parentCategoryUrl);

        if(cenaOdDo==null){
            cenaOdDo = "0 - "+maxPrice;
        }

        Integer totalPages = this.productService.findTotalPagesByCategoryUrlAndParentCategoryUrl(categoryUrl, parentCategoryUrl, produktiPoStrana,cenaOdDo);

        if(strana<0){
            strana = 0;
        }
        if(produktiPoStrana<=0){
            produktiPoStrana = 1;
        }

        if (strana >= totalPages && totalPages-1>=0) {
            strana = totalPages - 1;
        }

        if (categoryUrl.equals("produkti")) {
            productList = this.productService.findAllByParentCategoryUrl(parentCategoryUrl, sortiranje, strana, produktiPoStrana);
        } else {
            productList =
                    this.productService.findAllByCategoryUrlAndParentCategoryUrl(parentCategoryUrl, categoryUrl, sortiranje, strana, produktiPoStrana, cenaOdDo);
        }

        List<Integer> pageNumbers = new ArrayList<>();
        if (strana >= 3) {
            for (int i = Math.max(1, strana - 2 - (strana == totalPages ? 2 : strana == totalPages - 1 ? 1 : 0)); i <= Math.min(totalPages, strana + 2); i++) {
                pageNumbers.add(i);
            }
        } else {
            for (int i = 1; i <= Math.min(totalPages, 5); i++) {
                pageNumbers.add(i);
            }
        }
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("products", productList);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("priceFromTo", cenaOdDo);
        model.addAttribute("page", strana);
        model.addAttribute("pageSize", produktiPoStrana);
        model.addAttribute("sort", sortiranje);
        model.addAttribute("categoryUrl", categoryUrl);
        model.addAttribute("parentCategoryUrl", parentCategoryUrl);
        model.addAttribute("bodyContent", "shop-catalog");
        return "master-template";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long id, Model model) {
        this.categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }


}
