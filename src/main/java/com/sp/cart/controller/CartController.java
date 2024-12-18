package com.sp.cart.controller;

import com.sp.cart.dto.CartDTO;
import com.sp.cart.dto.CartItemDTO;
import com.sp.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long userId, @RequestBody CartItemDTO itemDTO) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, itemDTO));
    }

    @DeleteMapping("/{userId}/items/{bookId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, bookId));
    }
}
