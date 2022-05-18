package com.webshop.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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

    private Integer discountPercentage;

    private LocalDateTime isDealOfTheDayStartDateTime;

    @Column(nullable = false)
    private Boolean isNew; //5 dena da stoi

    @Column(nullable = false)
    private Boolean isOnDiscount;//da ne e na interval! Koga ke stiklira da go ima koga ke odstiklira da go nema

    @Column(nullable = false)
    private Boolean isDealOfTheDay; //otkako ke namesti, 24h da stoi na pocetna i da odbrojuva vreme(frontend)

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Image initialPhoto;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Image hoverPhoto;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "product")
    private List<Image> images;

    @ManyToOne
    private Category category;


    public Product() {
    }

    public Product(String name, String code, String description,
                   Integer stocks, Integer price, Integer discountPercentage, Boolean isNew,
                   Boolean isOnDiscount, Boolean isDealOfTheDay, Image initialPhoto,
                   Image hoverPhoto, List<Image> images, Category category) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.stocks = stocks;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.isNew = isNew;
        this.isOnDiscount = isOnDiscount;
        this.isDealOfTheDay = isDealOfTheDay;
        this.initialPhoto = initialPhoto;
        this.hoverPhoto = hoverPhoto;
        this.images = images;
        this.category = category;
        this.discountPrice = calculateDiscount();
    }

    public Integer calculateDiscount() {
        return isOnDiscount ? price - price * discountPercentage / 100 : price;
    }


    public String getPriceAsNumber(){
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price).replace(",",".");
    }


    public String calculateDiscountPrice() {
        int discPrice =isOnDiscount ? price - price * discountPercentage / 100 : price;
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(discPrice).replace(",",".");
    }

    public String calculateDiscountPrice(int quantity) {
        int discPrice = (isOnDiscount ? price - price * discountPercentage / 100 : price)*quantity;
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(discPrice).replace(",",".");
    }

    public String getInitialPhotoEncoded() {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(this.initialPhoto.getImage());
    }

    public String getHoverPhotoEncoded() {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(this.hoverPhoto.getImage());
    }

    @Transactional
    public List<String> getPhotosEncoded() {
        return this.images.stream()
                .map(t -> "data:image/png;base64," + Base64.getEncoder().encodeToString(t.getImage()))
                .collect(Collectors.toList());
    }

}
