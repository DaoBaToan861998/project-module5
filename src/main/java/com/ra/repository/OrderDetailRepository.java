package com.ra.repository;

import com.ra.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
List<OrderDetail> findByOrdersId(Long orderId);
Optional<OrderDetail> findByOrdersIdAndProductId(Long orderId,Long productId);
}
