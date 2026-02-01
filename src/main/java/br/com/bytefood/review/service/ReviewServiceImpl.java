package br.com.bytefood.review.service;

import br.com.bytefood.auth_users.entity.User;
import br.com.bytefood.auth_users.service.UserService;
import br.com.bytefood.enums.OrderStatus;
import br.com.bytefood.exception.BadRequestException;
import br.com.bytefood.exception.NotFoundException;
import br.com.bytefood.menu.entity.Menu;
import br.com.bytefood.menu.repository.MenuRepository;
import br.com.bytefood.order.entity.Order;
import br.com.bytefood.order.repository.OrderItemRepository;
import br.com.bytefood.order.repository.OrderRepository;
import br.com.bytefood.response.Response;
import br.com.bytefood.review.dtos.ReviewDTO;
import br.com.bytefood.review.entity.Review;
import br.com.bytefood.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Response<ReviewDTO> createReview(ReviewDTO reviewDTO) {
        log.info("Inside createReview()");

        User user = userService.getCurrentLoggedInUser();

        if (reviewDTO.getOrderId() == null || reviewDTO.getMenuId() == null){
            throw new BadRequestException("Order ID and Menu Item ID are required");
        }

        Menu menu = menuRepository.findById(reviewDTO.getMenuId())
                .orElseThrow(() -> new NotFoundException("Menu item not found"));

        Order order = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order item not found"));

        if (!order.getUser().getId().equals(user.getId())){
            throw new BadRequestException("This order doesn't belong to you");
        }

        if (order.getOrderStatus() != OrderStatus.DELIVERED){
            throw new BadRequestException("You can only review items from delivered orders");
        }

        boolean itemOrder = orderItemRepository.existsByOrderIdAndMenuId(
                reviewDTO.getOrderId(),
                reviewDTO.getMenuId());

        if (!itemOrder){
            throw new BadRequestException("This menu item was not part of the specified order");
        }

        if (reviewRepository.existsByUserIdAndMenuIdAndOrderId(
                user.getId(),
                reviewDTO.getMenuId(),
                reviewDTO.getOrderId())) {
            throw new BadRequestException("You've already reviewed this item from this order");
        }

        Review review = Review.builder()
                .user(user)
                .menu(menu)
                .orderId(reviewDTO.getOrderId())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        ReviewDTO responseDto = modelMapper.map(savedReview, ReviewDTO.class);
        responseDto.setUserName(user.getName());
        responseDto.setMenuName(menu.getName());

        return Response.<ReviewDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Review added successfully")
                .data(responseDto)
                .build();    }

    @Override
    public Response<List<ReviewDTO>> getReviewsForMenu(Long menuId) {
        log.info("Inside getReviewForMenu()");


        List<Review> reviews = reviewRepository.findByMenuIdOrderByIdDesc(menuId);

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();

        return Response.<List<ReviewDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reviews retrieved successfully")
                .data(reviewDTOs)
                .build();    }

    @Override
    public Response<Double> getAverageRating(Long menuId) {
        log.info("Inside getAverageRating()");

        Double averageRating = reviewRepository.calculateAverageRatingByMenuId(menuId);

        return Response.<Double>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Average rating retrieved successfully")
                .data(averageRating != null ? averageRating : 0.0)
                .build();    }
}
