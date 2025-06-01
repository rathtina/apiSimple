package org.springboot.authapi.ResponseEnities;

import lombok.Data;
import org.springboot.authapi.Enities.CartItem;
import org.springboot.authapi.Enities.Product;

@Data
public class CartItemResponse {
    private Integer productId;
    private String productName;
    private double price;
    private Integer quantity;
    private String imageUrl;
    private double totalPrice;

    public CartItemResponse(Integer productId, String productName, double price, Integer quantity, String imageUrl, double totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.totalPrice = totalPrice;
    }

    public static CartItemResponse fromEntity(CartItem item) {
        Product p=item.getProduct();

        return new CartItemResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                item.getQuantity(),
                p.getImageUrl(),
                p.getPrice()
        );
    }

}
