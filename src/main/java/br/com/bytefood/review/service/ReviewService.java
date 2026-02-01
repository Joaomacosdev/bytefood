package br.com.bytefood.review.service;

import br.com.bytefood.response.Response;
import br.com.bytefood.review.dtos.ReviewDTO;

import java.util.List;

public interface ReviewService {

    Response<ReviewDTO> createReview(ReviewDTO reviewDTO);
    Response<List<ReviewDTO>> getReviewsForMenu(Long menuId);
    Response<Double> getAverageRating(Long menuId);

}
