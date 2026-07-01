package org.northernarc.ecom.service;

import org.northernarc.ecom.DTO.ProductRequestDTO;
import org.northernarc.ecom.DTO.ProductResponseDTO;
import org.northernarc.ecom.DTO.ProductUpdateDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO saveProduct(ProductRequestDTO productDTO);

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Integer id);

    ProductResponseDTO updateProduct(Integer id,
                                     ProductUpdateDTO productDTO);

    void deleteProduct(Integer id);

    List<ProductResponseDTO> getProductsByCategory(String category);

    List<ProductResponseDTO> getProductsByBrand(String brand);

    List<ProductResponseDTO> getProductsByName(String name);
}