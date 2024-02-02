package com.devsuperior.DSCatalog.tests;

import com.devsuperior.DSCatalog.dto.ProductDTO;
import com.devsuperior.DSCatalog.entities.Category;
import com.devsuperior.DSCatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "phone", "good phone", 800.0, "img", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static Category createCategory(){
        return new Category(1L, "books");
    }

    public static ProductDTO createProductDto(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
