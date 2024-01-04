package com.ra.repository;

import com.ra.model.OrderStatus;
import com.ra.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
//Optional<Orders> findByStatusAndUserId(boolean status,Long userId);
//Optional<Orders> findByStatusAndUsersId(boolean status,Long userId);
Optional<Orders> findByDeliveryAndUsersId(boolean delivery,Long userId);
//Optional<Orders> findByOrderStatusAndUsersId(OrderStatus orderStatus,Long userId);
}
