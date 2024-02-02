package com.devsuperior.DSCatalog.repositories;

import com.devsuperior.DSCatalog.entities.Product;
import com.devsuperior.DSCatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long notExistingId;
    private Integer countProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        notExistingId = 100L;
        countProducts = 25;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnOptionalWithProductWhenIdExists(){

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalVoidWhenIdNotExists(){

        Optional<Product> result = repository.findById(notExistingId);

        Assertions.assertFalse(result.isPresent());
    }

}