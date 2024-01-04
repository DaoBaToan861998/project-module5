package com.ra.service;

import com.ra.dto.request.OrderDetailRequest;
import com.ra.dto.response.OrderDetailResponse;
import com.ra.dto.response.OrderResponse;

import java.util.List;

public interface IOrderDetailService {
OrderResponse addNewOrderDetail(OrderDetailRequest orderDetailRequest);
List<OrderDetailResponse> getAllOrderDetailByOrderId(Long orderId);
List<OrderDetailResponse> deleteOrderDetailByOrderId(Long orderId,Long orderDetail);
}
