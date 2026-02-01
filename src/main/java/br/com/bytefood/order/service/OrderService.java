package br.com.bytefood.order.service;

import br.com.bytefood.enums.OrderStatus;
import br.com.bytefood.order.dtos.OrderDTO;
import br.com.bytefood.order.dtos.OrderItemDTO;
import br.com.bytefood.response.Response;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Response<?> placeOrderFromCart();
    Response<OrderDTO> getOrderById(Long id);
    Response<Page<OrderDTO>> getAllOrder(OrderStatus orderStatus, int page, int size);
    Response<List<OrderDTO>> getOrdersOfUser();
    Response<OrderItemDTO> getOderItemById(Long orderItemId);
    Response<OrderDTO> updateOrderStatus(OrderDTO orderDTO);
    Response<Long> countUniqueCustomers();
}
