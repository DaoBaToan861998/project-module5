package com.ra.service.impl;

import com.ra.dto.request.OrderDetailRequest;
import com.ra.dto.response.OrderDetailResponse;
import com.ra.dto.response.OrderResponse;
import com.ra.model.OrderDetail;
import com.ra.model.Orders;
import com.ra.model.Product;
import com.ra.model.Users;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrderRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import com.ra.security.userprincal.UserPrinciple;
import com.ra.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

// add giỏ hàng
    @Override
    public OrderResponse addNewOrderDetail(OrderDetailRequest orderDetailRequest)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();


        Long userId=userPrinciple.getId();
        Users users =userRepository.findById(userId).orElseThrow(()->new RuntimeException("khong tim thay User"));
//         lấy đối tượng order vì có 2 trạng thái vừa là cart vừa là order (false là cart và true là là order)
        Optional<Orders> optionalOrders=orderRepository.findByDeliveryAndUsersId(false,userId);
        if(optionalOrders.isPresent()){

            Product product=productRepository.findById(orderDetailRequest.getProductId()).orElseThrow(()->new RuntimeException("không tìm thấy Product"));
//            trước khi thêm phải tìm xem OrderDetail có trong giỏ hàng chưa nêu có thì thay đổi số lượn nếu chưa thì khởi tạo OrderDetail
            Optional<OrderDetail> optionalOrderDetail=orderDetailRepository.findByOrdersIdAndProductId(optionalOrders.get().getId(), product.getId());
            if(optionalOrderDetail.isPresent()){
                OrderDetail oldOrderDetail=optionalOrderDetail.get();
                oldOrderDetail.setQuantity(oldOrderDetail.getQuantity()+orderDetailRequest.getQuantity());
                orderDetailRepository.save(oldOrderDetail);
            } else {
                OrderDetail orderDetail=OrderDetail.builder()
                        .orders(optionalOrders.get())
                        .price(product.getPrice())
                        .quantity(orderDetailRequest.getQuantity())
                        .product(product)
                        .build();
                orderDetailRepository.save(orderDetail);
            }
            return OrderResponse.builder()
                    .orderId(optionalOrders.get().getId())
                    .userName(optionalOrders.get().getUsers().getUserName())
                    .orderDetailResponses(orderDetailRepository.findByOrdersId(optionalOrders.get().getId()).stream().map(item->OrderDetailResponse.builder()
                            .id(item.getId())
                            .productName(item.getProduct().getProductName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .build()).toList())
                    .build();
        } else {
            // truong hop order khong ton tai
        }
        throw  new RuntimeException("không tìm thấy order");

    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetailByOrderId(Long orderId) {
        Orders orders=orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("không tìm thấy order"));
        List<OrderDetailResponse> list = orderDetailRepository.findByOrdersId(orderId).stream().map(item->OrderDetailResponse.builder()
                .id(item.getId())
                .productName(item.getProduct().getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build()).toList();
        return list;
    }

    @Override
    public List<OrderDetailResponse> deleteOrderDetailByOrderId(Long orderId, Long orderDetailId) {
        Orders orders=orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("không tìm thấy order"));

        orderDetailRepository.deleteById(orderDetailId);

        List<OrderDetailResponse> list=orderDetailRepository.findByOrdersId(orderId).stream().map(item->OrderDetailResponse.builder()
                .id(item.getId())
                .productName(item.getProduct().getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build()).toList();
        return list;
    }


}



