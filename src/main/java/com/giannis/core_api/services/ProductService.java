package com.giannis.core_api.services;

import com.giannis.core_api.dto.ProductRequestDto;
import com.giannis.core_api.dto.ProductResponseDto;
import java.util.List;

public interface ProductService {
    
    ProductResponseDto createProduct(ProductRequestDto productDto);
    
    ProductResponseDto getProductById(Long id);
    
    List<ProductResponseDto> getAllProducts();
    
    List<ProductResponseDto> getActiveProducts();
    
    ProductResponseDto updateProduct(Long id, ProductRequestDto productDto);
    
    void deleteProduct(Long id);
}