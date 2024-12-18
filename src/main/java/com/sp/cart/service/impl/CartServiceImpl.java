package com.sp.cart.service.impl;

import com.sp.cart.dto.CartDTO;
import com.sp.cart.dto.CartItemDTO;
import com.sp.cart.entity.Cart;
import com.sp.cart.entity.CartItem;
import com.sp.cart.repository.CartItemRepository;
import com.sp.cart.repository.CartRepository;
import com.sp.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private  CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        return toCartDTO(cart);
    }

    public CartDTO addItemToCart(Long userId, CartItemDTO itemDTO) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBookId().equals(itemDTO.getBookId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + itemDTO.getQuantity());
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            CartItem newItem = new CartItem();
            newItem.setBookId(itemDTO.getBookId());
            newItem.setPrice(itemDTO.getPrice());
            newItem.setQuantity(itemDTO.getQuantity());
            newItem.setSubtotal(itemDTO.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        updateCartTotal(cart);
        cartRepository.save(cart);

        return toCartDTO(cart);
    }

    public CartDTO removeItemFromCart(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        cartItemRepository.deleteByCartIdAndBookId(cart.getId(), bookId);

        cart.getCartItems().removeIf(item -> item.getBookId().equals(bookId));
        updateCartTotal(cart);
        cartRepository.save(cart);

        return toCartDTO(cart);
    }

    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }

    private Cart createNewCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setTotalAmount(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    private CartDTO toCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUserId());
        cartDTO.setTotalAmount(cart.getTotalAmount());

        List<CartItemDTO> items = cart.getCartItems().stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setBookId(item.getBookId());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());
            dto.setSubtotal(item.getSubtotal());
            return dto;
        }).collect(Collectors.toList());

        cartDTO.setItems(items);
        return cartDTO;
    }
}
