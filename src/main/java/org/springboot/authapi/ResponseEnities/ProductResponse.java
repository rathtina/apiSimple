package org.springboot.authapi.ResponseEnities;

import lombok.Data;
import lombok.Getter;
import org.springboot.authapi.Enities.Product;


@Data
public class ProductResponse {
    private Integer Id;
    private String name;
    private String description;
    private String imageUrl;
    private String category;
    private double price;
    private Integer stock;

    public ProductResponse(Product product) {
        this.Id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
