package com.webshop.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String code;

    private String description;

    @Column(nullable = false)
    private Integer stocks;

    @Column(nullable = false)
    private Integer price;

    private Integer discountPrice;

    private LocalDateTime isDealOfTheDayStartDateTime;

    @Column(nullable = false)
    private Boolean isNew; //5 dena da stoi

    @Column(nullable = false)
    private Boolean isOnDiscount;//da ne e na interval! Koga ke stiklira da go ima koga ke odstiklira da go nema

    @Column(nullable = false)
    private Boolean isDealOfTheDay; //otkako ke namesti, 24h da stoi na pocetna i da odbrojuva vreme(frontend)

    @Column(nullable = false)
    private String initialPhoto;

    private String hoverPhoto;

    @ElementCollection
    private List<String> images;

    @ManyToOne
    private Category category;

    public static final String UPLOAD_DIR = "/src/main/resources/static/assets/images/product-photos";

    public Product() {
        this.images = new ArrayList<>();
    }

    public Product(String name, String code, String description,
                   Integer stocks, Integer price, Integer discountPrice, Boolean isNew,
                   Boolean isOnDiscount, Boolean isDealOfTheDay, String initialPhoto,
                   String hoverPhoto, List<String> images, Category category) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.stocks = stocks;
        this.price = price;
        this.discountPrice = discountPrice;
        this.isNew = isNew;
        this.isOnDiscount = isOnDiscount;
        this.isDealOfTheDay = isDealOfTheDay;
        this.initialPhoto = initialPhoto;
        this.hoverPhoto = hoverPhoto;
        this.images = images;
        this.category = category;
    }

    public String getImagePath(){
        return "/assets/images/product-photos/"+this.id+"/";
    }

    public Integer calculateDiscountPrice(){
        return price- price*discountPrice/100;
    }

}
