package com.webshop.web.controller;

import com.webshop.model.Order;
import com.webshop.service.CategoryService;
import com.webshop.service.OrderService;
import com.webshop.service.ParentCategoryService;
import com.webshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ParentCategoryService parentCategoryService;
    private final ProductService productService;
    private final OrderService orderService;
    public AdminController(CategoryService categoryService, ParentCategoryService parentCategoryService, ProductService productService, OrderService orderService) {
        this.categoryService = categoryService;
        this.parentCategoryService = parentCategoryService;
        this.productService = productService;
        this.orderService = orderService;
    }
    @GetMapping({"","/login"})
    public String getLoginPage(@RequestParam(required = false) String error, Model model){
        if(error!=null){
            model.addAttribute("errorMessage", "Невалидно корисничко име или лозинка");
        }
        return "login";
    }

    @GetMapping("/products")
    public String getProductsPage(@RequestParam(required = false) Long id, Model model){
        model.addAttribute("bodyContent","admin-products");
        model.addAttribute("categories", this.categoryService.findAllCategoriesProjection());
        model.addAttribute("products", this.productService.findAllValidProductProjection());
        if(id!=null){
            model.addAttribute("product", this.productService.findById(id));
        }
        return "admin-master-template";
    }
    @GetMapping("/categories")
    public String getCategoriesPage(@RequestParam(required = false) Long id,@RequestParam(required=false) String error, Model model){
        model.addAttribute("bodyContent","admin-categories");
        model.addAttribute("categories",this.categoryService.findAllCategoriesProjection());
        model.addAttribute("parentCategories", this.parentCategoryService.findAll());
        if(id!=null){
            model.addAttribute("category", this.categoryService.findById(id));
        }
        return "admin-master-template";
    }
    @GetMapping("/parent-categories")
    public String getParentCategoriesPage(@RequestParam(required = false) Long id,@RequestParam(required = false) String error, Model model){
        model.addAttribute("bodyContent","admin-parent-categories");
        model.addAttribute("parentCategories", this.parentCategoryService.findAll());
        if(error!=null){
            model.addAttribute("errorMessage", "Главната категорија веќе постои");
        }
        if(id!=null){
            model.addAttribute("parentCategory", this.parentCategoryService.findById(id));
        }
        return "admin-master-template";
    }
    @GetMapping("/orders")
    public String getOrdersPage(Model model){
        model.addAttribute("bodyContent","admin-orders");
        model.addAttribute("orders", this.orderService.listAll().stream().filter(t->!t.getIsConfirmed()).collect(Collectors.toList()));
        return "admin-master-template";
    }

    @GetMapping("/accepted-orders")
    public String getAcceptedOrdersPage(Model model){
        model.addAttribute("bodyContent","admin-accepted-orders");
        model.addAttribute("orders", this.orderService.listAll().stream().filter(Order::getIsConfirmed).collect(Collectors.toList()));
        return "admin-master-template";
    }

}
