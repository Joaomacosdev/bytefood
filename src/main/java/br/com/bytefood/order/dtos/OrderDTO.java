package br.com.bytefood.order.dtos;

import br.com.bytefood.auth_users.dtos.UserDTO;
import br.com.bytefood.enums.OrderStatus;
import br.com.bytefood.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {

    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private UserDTO user;

    private List<OrderItemDTO> orderItems;
}
