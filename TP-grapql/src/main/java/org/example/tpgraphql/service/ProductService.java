package org.example.tpgraphql.service;

import lombok.RequiredArgsConstructor;
import org.example.tpgraphql.entity.Category;
import org.example.tpgraphql.entity.Product;
import org.example.tpgraphql.repository.CategoryRepository;
import org.example.tpgraphql.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product create(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) return null;

        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product update(Long productId, Product updated) {
        return productRepository.findById(productId).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setStock(updated.getStock());
            return productRepository.save(existing);
        }).orElse(null);
    }

    public boolean delete(Long productId) {
        if (!productRepository.existsById(productId)) {
            return false;
        }
        productRepository.deleteById(productId);
        return true;
    }
}