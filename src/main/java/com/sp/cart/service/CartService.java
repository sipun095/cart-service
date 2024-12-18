package com.sp.cart.service;

import com.sp.cart.dto.CartDTO;
import com.sp.cart.dto.CartItemDTO;

public interface CartService {
    CartDTO getCart(Long userId);

    CartDTO addItemToCart(Long userId, CartItemDTO itemDTO);

    CartDTO removeItemFromCart(Long userId, Long bookId);
}
