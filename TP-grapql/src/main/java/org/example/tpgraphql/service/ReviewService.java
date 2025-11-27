package org.example.tpgraphql.service;

import lombok.RequiredArgsConstructor;
import org.example.tpgraphql.entity.Product;
import org.example.tpgraphql.entity.Review;
import org.example.tpgraphql.repository.ProductRepository;
import org.example.tpgraphql.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> findByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review create(Review review, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return null;

        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public Review update(Long id, Review updated) {
        return reviewRepository.findById(id).map(existing -> {
            existing.setAuthor(updated.getAuthor());
            existing.setComment(updated.getComment());
            existing.setRating(updated.getRating());
            return reviewRepository.save(existing);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            return false;
        }
        reviewRepository.deleteById(id);
        return true;
    }
}