package com.webshop.model.exceptions;

public class CategoryAlreadyExists extends RuntimeException{
    public CategoryAlreadyExists(){
        super("Category already exists");
    }
}
