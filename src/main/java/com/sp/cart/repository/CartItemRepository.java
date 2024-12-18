package com.sp.cart.repository;

import com.sp.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    void deleteByCartIdAndBookId(Long id, Long bookId);
}
