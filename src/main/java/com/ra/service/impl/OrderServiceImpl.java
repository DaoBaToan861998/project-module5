package com.ra.service.impl;

import com.ra.dto.request.CreateOrder;
import com.ra.dto.response.ListOrder;
import com.ra.model.OrderStatus;
import com.ra.model.Orders;
import com.ra.model.Users;
import com.ra.repository.OrderRepository;
import com.ra.repository.UserRepository;
import com.ra.security.userprincal.UserPrinciple;
import com.ra.service.IOrderService;
import com.ra.advice.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;



    @Override
    public Orders addNewOrderToUserByIdUser(CreateOrder createOrder) throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();

        Optional<Users> optionalUsers = userRepository.findById(userPrinciple.getId());
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            Orders orders = Orders.builder()
                    .addressShip(createOrder.getAddressShip())
                    .phone(createOrder.getPhone())
                    .createAt(createOrder.getCreateAt())
                    .delivery(false)
                    .orderStatus(OrderStatus.PREPARING)
                    .note(createOrder.getNote())
                    .users(users)
                    .build();
            return orderRepository.save(orders);
        }
        throw  new CustomException("user not found");
    }


    @Override
    public List<ListOrder> getAllOrders() {
        List<ListOrder> orders=orderRepository.findAll().stream().map(item->ListOrder.builder()
                .ordersId(item.getId())
                .addressShip(item.getAddressShip())
                .phone(item.getPhone())
                .userName(item.getUsers().getUserName())
                .createAt(item.getCreateAt())
                .delivery(item.getDelivery())
                .orderStatus(item.getOrderStatus())
                .note(item.getNote()).build()
               ).toList();
        return orders;
    }

    @Override
    public void updateOrderDelivery(Long orderId) throws CustomException {
        Orders orders=orderRepository.findById(orderId).orElseThrow(()->new  CustomException("order not found"));
        orders.setDelivery(true);
        orderRepository.save(orders);
    }

    @Override
    public void updateOrderStatus(Long orderId, String orderStatus) throws CustomException {
        Orders orders=orderRepository.findById(orderId).orElseThrow(()-> new CustomException("order not found"));

         switch (orderStatus){
             case "1":
                orders.setOrderStatus(OrderStatus.SUCCESS);
                break;
             case "2":
                 orders.setOrderStatus(OrderStatus.RETURNED);
                 break;
             case "3":
                 orders.setOrderStatus(OrderStatus.PREPARING);
                 break;
             case "4":
                 orders.setOrderStatus(OrderStatus.SHIPPED);
                 break;
             case "5":
                 orders.setOrderStatus(OrderStatus.FAILED);
                 break;
             default:
                 System.out.println("Unknown order status: " + orderStatus);
         }
         orderRepository.save(orders);
    }



}
