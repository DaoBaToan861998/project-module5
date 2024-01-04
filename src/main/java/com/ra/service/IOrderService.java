package com.ra.service;

import com.ra.dto.request.CreateOrder;
import com.ra.dto.response.ListOrder;
import com.ra.model.OrderStatus;
import com.ra.model.Orders;
import com.ra.advice.CustomException;

import java.util.List;

public interface IOrderService {
//    Orders addNewOrderToUserByIdUser(Long userId, CreateOrder createOrder) throws CustomException;
    Orders addNewOrderToUserByIdUser( CreateOrder createOrder) throws CustomException;
List<ListOrder> getAllOrders();
    void updateOrderDelivery(Long orderId) throws CustomException;

    void updateOrderStatus(Long orderId, String orderStatus) throws CustomException;


}
