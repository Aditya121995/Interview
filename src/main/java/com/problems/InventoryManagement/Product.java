package com.problems.InventoryManagement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Product {
    private final String productId;
    private final String productName;
    @Setter
    private String productDescription;
    @Setter
    private double productPrice;
    private final ProductCategory productCategory;

    public Product(String productId, String productName, String productDescription,
                   double productPrice, ProductCategory productCategory) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }
}
