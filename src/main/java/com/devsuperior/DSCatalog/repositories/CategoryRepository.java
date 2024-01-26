package com.devsuperior.DSCatalog.repositories;

import com.devsuperior.DSCatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
