package br.com.bytefood.payment.dtos;

import br.com.bytefood.auth_users.dtos.UserDTO;
import br.com.bytefood.auth_users.entity.User;
import br.com.bytefood.enums.PaymentGateway;
import br.com.bytefood.enums.PaymentStatus;
import br.com.bytefood.order.dtos.OrderDTO;
import br.com.bytefood.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {


    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private String transactionId;

    private PaymentGateway paymentGateway;

    private String failureReasons;

    private Boolean success;

    private LocalDateTime paymentDate;

    private OrderDTO order;
    private UserDTO user;
}
