package com.webshop.model.exceptions;

public class ParentCategoryNotFound extends RuntimeException{
    public ParentCategoryNotFound(Long id) {
        super(String.format("Parent Category with id $d was not found", id));
    }

}
