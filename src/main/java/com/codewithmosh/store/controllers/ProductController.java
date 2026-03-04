package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.dtos.UpdateProductRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;



    @GetMapping("/all")
    public List<ProductDto> getAllProduct(
            @RequestParam(name = "categoryID", required = false) Byte categoryId
    ) {
        List<Product> products;

        if(categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else{
            products = productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper::productToProductDto).toList();

        }

        @GetMapping("/{id}")
        public ResponseEntity<ProductDto> getProduct(@PathVariable Long id)
        {
            var product = productRepository.findById(id).orElse(null);
            if(product == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(productMapper.productToProductDto(product));
        }


        @PostMapping("/create")
        public ResponseEntity <ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder)
        {
            var category = categoryRepository.findById(Byte.valueOf(productDto.getCategoryId())).orElse(null);
            if(category == null){
                return ResponseEntity.badRequest().build();
            }
            var product = productMapper.toEntity(productDto);

            product.setCategory(category);
            productRepository.save(product);
            productDto.setId(product.getId());

            var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
            return ResponseEntity.created(uri).body(productDto);

        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ProductDto> deleteProduct(@PathVariable(name = "id") Long id)
        {
        var product = productRepository.findById(id).orElse(null);

        if(product == null){
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.ok(productMapper.productToProductDto(product));

        }

        @PutMapping("/{id}")
        public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long id, @RequestBody UpdateProductRequest request)
        {
            var product = productRepository.findById(id).orElse(null);
            if(product == null){
                return ResponseEntity.notFound().build();
            }
            var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            product.setCategory(category);

            productMapper.update(request, product);
            productRepository.save(product);

            return ResponseEntity.ok(productMapper.productToProductDto(product));

        }

    }

