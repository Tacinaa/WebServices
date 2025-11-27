package org.example.tpgraphql.controller;

import lombok.RequiredArgsConstructor;
import org.example.tpgraphql.entity.Product;
import org.example.tpgraphql.service.ProductService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductGraphQLController {

    private final ProductService productService;

    @QueryMapping
    public List<Product> products() {
        return productService.findAll();
    }

    @QueryMapping
    public Product product(@Argument Long id) {
        return productService.findById(id);
    }

    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productService.findByCategory(categoryId);
    }

    @MutationMapping
    public Product createProduct(@Argument String name,
                                 @Argument String description,
                                 @Argument Double price,
                                 @Argument Integer stock,
                                 @Argument Long categoryId) {

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price != null ? BigDecimal.valueOf(price) : null)
                .stock(stock)
                .build();

        return productService.create(product, categoryId);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id,
                                 @Argument String name,
                                 @Argument String description,
                                 @Argument Double price,
                                 @Argument Integer stock) {

        Product updated = Product.builder()
                .name(name)
                .description(description)
                .price(price != null ? BigDecimal.valueOf(price) : null)
                .stock(stock)
                .build();

        return productService.update(id, updated);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        return productService.delete(id);
    }
}