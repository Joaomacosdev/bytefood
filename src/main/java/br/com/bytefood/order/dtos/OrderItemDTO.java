package br.com.bytefood.order.dtos;

import br.com.bytefood.menu.dtos.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDTO {

    private Long id;

    private Long menuId;

    private MenuDTO menu;

    private Integer quantity;

    private BigDecimal pricePerUnit;

    private BigDecimal subtotal;
}
