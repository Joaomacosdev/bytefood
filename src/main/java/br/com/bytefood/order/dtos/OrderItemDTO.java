package br.com.bytefood.order.dtos;

import br.com.bytefood.menu.dtos.MenuDTO;
import br.com.bytefood.menu.entity.Menu;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDTO {

    private Long id;

    private Menu menuId;

    private MenuDTO menu;

    private Integer quantity;

    private BigDecimal pricePerUnit;

    private BigDecimal subTotal;
}
