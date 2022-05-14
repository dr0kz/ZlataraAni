package com.webshop.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ParentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.REMOVE)
    private List<Category> categories;

    public ParentCategory() {
    }

    public ParentCategory(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
