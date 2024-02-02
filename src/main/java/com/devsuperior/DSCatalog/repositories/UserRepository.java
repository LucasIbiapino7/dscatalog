package com.devsuperior.DSCatalog.repositories;

import com.devsuperior.DSCatalog.entities.Product;
import com.devsuperior.DSCatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
