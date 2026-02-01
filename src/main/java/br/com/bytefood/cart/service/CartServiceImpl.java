package br.com.bytefood.cart.service;

import br.com.bytefood.auth_users.entity.User;
import br.com.bytefood.auth_users.service.UserService;
import br.com.bytefood.cart.dtos.CartDTO;
import br.com.bytefood.cart.entity.Cart;
import br.com.bytefood.cart.entity.CartItem;
import br.com.bytefood.cart.repository.CartItemRepository;
import br.com.bytefood.cart.repository.CartRepository;
import br.com.bytefood.exception.NotFoundException;
import br.com.bytefood.menu.entity.Menu;
import br.com.bytefood.menu.repository.MenuRepository;
import br.com.bytefood.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> addItemCart(CartDTO cartDTO) {
        log.info("Inside addItemCart()");

        Long menuId = cartDTO.getMenuId();
        int quantity = cartDTO.getQuantity();

        User user = userService.getCurrentLoggedInUser();

        Menu menu = menuRepository.findById(cartDTO.getMenuId())
                .orElseThrow(() -> new NotFoundException("Menu Item not found"));

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });


        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getMenu().getId().equals(menuId))
                .findFirst();

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setSubTotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .menu(menu)
                    .quantity(quantity)
                    .pricePerUnit(menu.getPrice())
                    .subTotal(menu.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();

            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        //cartRepository.save(cart);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item added to cart successfully")
                .build();
    }

    @Override
    public Response<?> incrementItem(Long menuId) {
        log.info("Inside incrementItem()");

        User user = userService.getCurrentLoggedInUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("cart not found in cart"));


        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getId().equals(menuId))
                .findFirst().orElseThrow(() -> new NotFoundException("Cart item not found in cart"));

        int newQuantity = cartItem.getQuantity() + 1;

        cartItem.setQuantity(newQuantity);
        cartItem.setSubTotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(newQuantity)));

        cartItemRepository.save(cartItem);


        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity incremented successfully")
                .build();
    }

    @Override
    public Response<?> decrementItem(Long menuId) {
        log.info("Inside decrementItem()");


        User user = userService.getCurrentLoggedInUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("cart not found in cart"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getId().equals(menuId))
                .findFirst().orElseThrow(() -> new NotFoundException("Cart item not found in cart"));

        int newQuantity = cartItem.getQuantity() - 1;

        if (newQuantity > 0) {
            cartItem.setQuantity(newQuantity);
            cartItem.setSubTotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(newQuantity)));
            cartItemRepository.save(cartItem);
        } else {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity decrement successfully")
                .build();
    }

    @Override
    public Response<?> removeItem(Long cartItemId) {
        log.info("Inside removeItem()");

        User user = userService.getCurrentLoggedInUser();
        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("cart not found in cart"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Car Item not found"));

        if (!cart.getCartItems().contains(cartItem)) {
            throw new NotFoundException("Cart item does belong to this user's cart");
        }
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity decrement successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response<CartDTO> getShoppingItem() {
        log.info("Inside getShoppingItem()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("cart not found in cart"));

        List<CartItem> cartItems = cart.getCartItems();

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                totalAmount = totalAmount.add(item.getSubTotal());
            }
        }

        cartDTO.setTotalAmount(totalAmount);

        if (cartDTO.getCartItems() != null) {
            cartDTO.getCartItems()
                    .forEach(item -> item.getMenu().setReviews(null));
        }

        return Response.<CartDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item get Shopping Item  successfully")
                .data(cartDTO)
                .build();
    }

    @Override
    public Response<?> clearShoppingCart() {
        log.info("Inside clearShoppingCart()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new NotFoundException("cart not found in cart"));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();

        cartRepository.save(cart);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item clear Shopping Cart successfully")
                .build();
    }
}
