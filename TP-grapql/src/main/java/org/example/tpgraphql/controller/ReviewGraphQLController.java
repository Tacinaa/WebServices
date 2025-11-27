package org.example.tpgraphql.controller;

import lombok.RequiredArgsConstructor;
import org.example.tpgraphql.entity.Review;
import org.example.tpgraphql.service.ReviewService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewGraphQLController {

    private final ReviewService reviewService;

    @QueryMapping
    public List<Review> reviews() {
        return reviewService.findAll();
    }

    @QueryMapping
    public Review review(@Argument Long id) {
        return reviewService.findById(id);
    }

    @QueryMapping
    public List<Review> reviewsByProduct(@Argument Long productId) {
        return reviewService.findByProduct(productId);
    }

    @MutationMapping
    public Review createReview(@Argument Long productId,
                               @Argument String author,
                               @Argument String comment,
                               @Argument Integer rating) {

        Review review = Review.builder()
                .author(author)
                .comment(comment)
                .rating(rating)
                .build();

        return reviewService.create(review, productId);
    }

    @MutationMapping
    public Review updateReview(@Argument Long id,
                               @Argument String author,
                               @Argument String comment,
                               @Argument Integer rating) {

        Review updated = Review.builder()
                .author(author)
                .comment(comment)
                .rating(rating)
                .build();

        return reviewService.update(id, updated);
    }

    @MutationMapping
    public Boolean deleteReview(@Argument Long id) {
        return reviewService.delete(id);
    }
}