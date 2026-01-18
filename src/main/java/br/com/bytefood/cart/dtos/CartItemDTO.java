package br.com.bytefood.cart.dtos;

import br.com.bytefood.menu.dtos.MenuDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemDTO {

    private Long id;

    private MenuDTO menu;

    private Integer quantity;

    private BigDecimal pricePerUnit;

    private BigDecimal subTotal;

}
