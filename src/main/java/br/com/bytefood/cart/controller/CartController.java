package br.com.bytefood.cart.controller;

import br.com.bytefood.cart.dtos.CartDTO;
import br.com.bytefood.cart.service.CartService;
import br.com.bytefood.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<Response<?>> addItemCart(@RequestBody @Valid CartDTO cartDTO){
        return ResponseEntity.ok(cartService.addItemCart(cartDTO));
    }

    @PutMapping("/items/increment/{menuId}")
    public ResponseEntity<Response<?>> incrementItem(@PathVariable Long menuId){
        return ResponseEntity.ok(cartService.incrementItem(menuId));
    }

    @PutMapping("/items/decrement/{menuId}")
    public ResponseEntity<Response<?>> decrementItem(@PathVariable Long menuId){
        return ResponseEntity.ok(cartService.decrementItem(menuId));
    }

    @DeleteMapping("/items/{carItemId}")
    public ResponseEntity<Response<?>> removeItem(@PathVariable Long carItemId){
        return ResponseEntity.ok(cartService.removeItem(carItemId));
    }

    @GetMapping
    public ResponseEntity<Response<CartDTO>> getShoppingItem(){
        return ResponseEntity.ok(cartService.getShoppingCart());
    }


    @DeleteMapping
    public ResponseEntity<Response<?>> clearShoppingCart(){
        return ResponseEntity.ok(cartService.clearShoppingCart());
    }


}
