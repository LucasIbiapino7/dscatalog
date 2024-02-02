package com.devsuperior.DSCatalog.services;

import com.devsuperior.DSCatalog.dto.ProductDTO;
import com.devsuperior.DSCatalog.entities.Category;
import com.devsuperior.DSCatalog.entities.Product;
import com.devsuperior.DSCatalog.repositories.CategoryRepository;
import com.devsuperior.DSCatalog.repositories.ProductRepository;
import com.devsuperior.DSCatalog.services.exception.DatabaseException;
import com.devsuperior.DSCatalog.services.exception.ResourceNotFoundException;
import com.devsuperior.DSCatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;//componente que iremos testar

    @Mock
    private ProductRepository repository;//componentes  que a classe precisa, mas que não podem ser injetados

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long notExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        notExistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDto();
        category = Factory.createCategory();

        //Configurando o Mockito
        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(notExistingId)).thenReturn(false);

        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(notExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(notExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(notExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }


    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class,() -> {
            service.delete(notExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () -> {
           service.delete(dependentId);
        });
    }

    @Test
    public void findAllShouldReturnPage(){
        Pageable pageable = Pageable.ofSize(10);

        Page<ProductDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenExistingId(){

        ProductDTO productDTO = service.findById(existingId);

        Assertions.assertNotNull(productDTO);
        Mockito.verify(repository).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNotExistingId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           service.findById(notExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductDtoWhenExistingId(){

        ProductDTO dto = service.update(existingId, productDTO);

        Assertions.assertNotNull(dto);
        Mockito.verify(repository).getReferenceById(existingId);
        Mockito.verify(repository).save(product);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenNotExistingId(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           service.update(notExistingId, productDTO);
        });

    }

}