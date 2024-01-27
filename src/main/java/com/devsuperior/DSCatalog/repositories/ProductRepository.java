package com.devsuperior.DSCatalog.repositories;

import com.devsuperior.DSCatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
