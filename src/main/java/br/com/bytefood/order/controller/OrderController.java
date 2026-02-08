package br.com.bytefood.order.controller;

import br.com.bytefood.enums.OrderStatus;
import br.com.bytefood.order.dtos.OrderDTO;
import br.com.bytefood.order.dtos.OrderItemDTO;
import br.com.bytefood.order.service.OrderService;
import br.com.bytefood.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Response<?>> checkout(){
        return ResponseEntity.ok(orderService.placeOrderFromCart());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OrderDTO>> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyOrders(){
        return ResponseEntity.ok(orderService.getOrdersOfUser());
    }

    @GetMapping("/order-item/{orderItemId}")
    public ResponseEntity<Response<OrderItemDTO>> getOderItemById(@PathVariable Long orderItemId){
        return ResponseEntity.ok(orderService.getOderItemById(orderItemId));
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<Page<OrderDTO>>> getMyOrders(
            @RequestParam(required = false)OrderStatus orderStatus,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "100")int size
            ){
        return ResponseEntity.ok(orderService.getAllOrder(orderStatus, page, size));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<?>> updateOrderStatus(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.updateOrderStatus(orderDTO));
    }

    @GetMapping("/unique-customers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response<Long>> countUniqueCustomers(){
        return ResponseEntity.ok(orderService.countUniqueCustomers());
    }

}
