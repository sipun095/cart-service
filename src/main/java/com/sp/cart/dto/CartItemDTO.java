package com.sp.cart.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long bookId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
