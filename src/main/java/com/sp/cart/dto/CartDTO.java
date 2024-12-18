package com.sp.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartDTO {
    private Long userId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;

}
