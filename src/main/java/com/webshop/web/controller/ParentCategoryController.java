package com.webshop.web.controller;

import com.webshop.model.exceptions.ParentCategoryAlreadyExists;
import com.webshop.service.ParentCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/parent-categories")
public class ParentCategoryController {

    private final ParentCategoryService parentCategoryService;

    public ParentCategoryController(ParentCategoryService parentCategoryService) {
        this.parentCategoryService = parentCategoryService;
    }

    @PostMapping("/add")
    public String createParentCategory(@RequestParam(required = false) Long id,@RequestParam String parentCategoryName, @RequestParam String parentCategoryUrlName){
        try{
            if(id!=null){
                this.parentCategoryService.editParentCategory(id, parentCategoryName, parentCategoryUrlName);
            }else{
                this.parentCategoryService.createParentCategory(parentCategoryName, parentCategoryUrlName);
            }
            return "redirect:/admin/parent-categories";
        }catch(ParentCategoryAlreadyExists exists){
            return "redirect:/admin/parent-categories?error=";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteParentCategory(@PathVariable Long id){
        this.parentCategoryService.deleteParentCategory(id);
        return "redirect:/admin/parent-categories";
    }
}
