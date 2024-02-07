package com.devsuperior.DSCatalog.repositories;

import com.devsuperior.DSCatalog.entities.Product;
import com.devsuperior.DSCatalog.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query(nativeQuery = true, value = """
            SELECT * FROM (
            SELECT DISTINCT tb_product.name, tb_product.id
            FROM tb_product
            INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
            WHERE (:categoryIds IS NULL OR category_id IN :categoryIds) AND LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))
            ) AS tb_result
            """, countQuery = """
            SELECT COUNT(*) FROM (
            SELECT DISTINCT tb_product.name, tb_product.id
            FROM tb_product
            INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
            WHERE (:categoryIds IS NULL OR category_id IN :categoryIds) AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%', :name, '%'))
            ) AS tb_result
            """)
    Page<ProductProjection> searchProducts(List<Long> categoryIds, String name, Pageable pageable);


    @Query("SELECT obj " +
            "FROM Product obj " +
            "JOIN FETCH obj.categories " +
            "WHERE obj.id IN :productsIds " +
            "ORDER BY obj.name")
    List<Product> searchProductsWithCategories(List<Long> productsIds);
}
