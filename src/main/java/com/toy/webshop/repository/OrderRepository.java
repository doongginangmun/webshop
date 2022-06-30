package com.toy.webshop.repository;

import com.toy.webshop.dto.OrderDto;
import com.toy.webshop.dto.OrderItemDto;
import com.toy.webshop.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long userId);

    @Query("select new com.toy.webshop.dto.OrderDto(o.id, o.orderDate, o.status, o.orderPrice, d.address)" +
            " from Order o join o.user u join o.delivery d where  u.id =:userId")
    List<OrderDto> findOrders(@Param("userId") Long userId);

    @Query("select new com.toy.webshop.dto.OrderItemDto(i.name, oi.orderPrice, oi.count)" +
            " from OrderItem oi join oi.item i where oi.order.id =:orderId")
    List<OrderItemDto> findOrderItems(@Param("orderId") Long orderId);

}
