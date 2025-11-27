package org.example.tpgraphql.controller;

import lombok.RequiredArgsConstructor;
import org.example.tpgraphql.entity.Category;
import org.example.tpgraphql.service.CategoryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryGraphQLController {

    private final CategoryService categoryService;

    @QueryMapping
    public List<Category> categories() {
        return categoryService.findAll();
    }

    @QueryMapping
    public Category category(@Argument Long id) {
        return categoryService.findById(id);
    }

    @MutationMapping
    public Category createCategory(@Argument String name,
                                   @Argument String description) {
        Category category = Category.builder()
                .name(name)
                .description(description)
                .build();
        return categoryService.create(category);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id,
                                   @Argument String name,
                                   @Argument String description) {
        Category updated = Category.builder()
                .name(name)
                .description(description)
                .build();
        return categoryService.update(id, updated);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        return categoryService.delete(id);
    }
}