package com.devsuperior.DSCatalog.services;

import com.devsuperior.DSCatalog.dto.CategoryDTO;
import com.devsuperior.DSCatalog.dto.ProductDTO;
import com.devsuperior.DSCatalog.entities.Category;
import com.devsuperior.DSCatalog.entities.Product;
import com.devsuperior.DSCatalog.projections.ProductProjection;
import com.devsuperior.DSCatalog.repositories.CategoryRepository;
import com.devsuperior.DSCatalog.repositories.ProductRepository;
import com.devsuperior.DSCatalog.services.exception.DatabaseException;
import com.devsuperior.DSCatalog.services.exception.ResourceNotFoundException;
import com.devsuperior.DSCatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, String categoryId, Pageable pageable){

        List<Long> categoryIds = Arrays.asList();

        if (!categoryId.isEmpty()){
            String[] vet = categoryId.split(",");
            List<String> result = Arrays.asList(vet);
            categoryIds = result.stream().map(Long::parseLong).toList();
            //categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
        }

        Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);

        List<Product> entities = repository.searchProductsWithCategories(page.map(x -> x.getId()).toList());
        entities = Utils.replace(page.getContent(), entities);

        List<ProductDTO> productDTOS = entities.stream().map(x -> new ProductDTO(x, x.getCategories())).toList();

        PageImpl<ProductDTO> result = new PageImpl<>(productDTOS, page.getPageable(), page.getTotalElements());
        return result;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product product = new Product();
        copyDtoToEntity(dto, product);
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product product = repository.getReferenceById(id);
            copyDtoToEntity(dto, product);
            product = repository.save(product);
            return new ProductDTO(product);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Resource Not Found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Id não encontrado");
        }
        try{
            repository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

    private void copyDtoToEntity(ProductDTO dto, Product product) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImgUrl(dto.getImgUrl());
        product.setDate(dto.getDate());
        product.getCategories().clear();
        for (CategoryDTO cat : dto.getCategories()){
            Category category = categoryRepository.getReferenceById(cat.getId());
            product.getCategories().add(new Category(category.getId(), category.getName()));
        }
    }
}
