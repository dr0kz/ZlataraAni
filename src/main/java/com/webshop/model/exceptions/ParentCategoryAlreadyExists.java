package com.webshop.model.exceptions;

public class ParentCategoryAlreadyExists extends RuntimeException{
    public ParentCategoryAlreadyExists() {
        super("The Parent Category Already Exists");
    }

}

