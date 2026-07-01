//package org.northernarc.ecomapplication.service;
//
//import org.northernarc.ecomapplication.model.Product;
//import org.northernarc.ecomapplication.repository.ProductDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Locale;
//
//@Service
//public class ProductServiceImpl implements ProductService{
//    @Autowired
//    private ProductDao productDao;
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @Override
//    public Product saveProduct(Product product) {
//        return productDao.save(product);
//    }
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    @Override
//    public Product getProduct(Long id) {
//        return productDao.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
//    }
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    @Override
//    public List<Product> getAllProducts() {
//        return productDao.findAll();
//    }
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    @Override
//    public List<Product> searchProducts(String name, String category, String brand) {
//        String safeName = name == null ? "" : name.toLowerCase(Locale.ROOT).trim();
//        String safeCategory = category == null ? "" : category.toLowerCase(Locale.ROOT).trim();
//        String safeBrand = brand == null ? "" : brand.toLowerCase(Locale.ROOT).trim();
//
//        return productDao.findAll().stream()
//                .filter(product -> safeName.isEmpty() || containsIgnoreCase(product.getName(), safeName))
//                .filter(product -> safeCategory.isEmpty() || containsIgnoreCase(product.getCategory(), safeCategory))
//                .filter(product -> safeBrand.isEmpty() || containsIgnoreCase(product.getBrand(), safeBrand))
//                .toList();
//    }
//
//    private boolean containsIgnoreCase(String value, String search) {
//        return value != null && value.toLowerCase(Locale.ROOT).contains(search);
//    }
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    @Override
//    public List<Product> getProductsByName(String name) {
//        return productDao.findByNameContainingIgnoreCase(name);
//    }
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    @Override
//    public List<Product> getProductsByCategory(String category) {
//        return productDao.findByCategoryContainingIgnoreCase(category);
//    }
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    @Override
//    public List<Product> getProductsByBrand(String brand) {
//        return productDao.findByBrandContainingIgnoreCase(brand);
//    }
//    @PreAuthorize("hasRole('ADMIN')")
//    @Override
//    public void deleteProduct(Long id) {
//        productDao.deleteById(id);
//    }
//}
package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Product;
import org.northernarc.ecomapplication.repository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Product saveProduct(Product product) {
        return productDao.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productDao.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    public List<Product> searchProducts(String name,
                                        String category,
                                        String brand) {

        String safeName =
                name == null ? "" : name.toLowerCase(Locale.ROOT).trim();

        String safeCategory =
                category == null ? "" : category.toLowerCase(Locale.ROOT).trim();

        String safeBrand =
                brand == null ? "" : brand.toLowerCase(Locale.ROOT).trim();

        return productDao.findAll()
                .stream()
                .filter(product ->
                        safeName.isEmpty()
                                || containsIgnoreCase(product.getName(), safeName))
                .filter(product ->
                        safeCategory.isEmpty()
                                || containsIgnoreCase(product.getCategory(), safeCategory))
                .filter(product ->
                        safeBrand.isEmpty()
                                || containsIgnoreCase(product.getBrand(), safeBrand))
                .toList();
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productDao.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productDao.findByCategoryContainingIgnoreCase(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productDao.findByBrandContainingIgnoreCase(brand);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Long id) {
        productDao.deleteById(id);
    }

    private boolean containsIgnoreCase(String value, String search) {
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(search);
    }
}