package com.webshop.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String urlName;

    @ManyToOne
    private ParentCategory parentCategory;

    public Category() {
    }

    public Category(String name, ParentCategory parentCategory, String urlName) {
        this.name = name;
        this.urlName = urlName;
        this.parentCategory = parentCategory;
    }
}
