package br.com.bytefood.cart.service;

import br.com.bytefood.cart.dtos.CartDTO;
import br.com.bytefood.response.Response;

public interface CartService {

    Response<?> addItemCart(CartDTO cartDTO);
    Response<?> incrementItem(Long menuId);
    Response<?> decrementItem(Long menuId);
    Response<?> removeItem(Long cartItemId);
    Response<CartDTO> getShoppingCart();
    Response<?> clearShoppingCart();

}
