package com.ra.controller;

import com.ra.dto.request.CreateOrder;
import com.ra.dto.response.ListOrder;
import com.ra.model.Orders;
import com.ra.service.IOrderService;
import com.ra.advice.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @PostMapping("/user/addOrder")
    public ResponseEntity<Orders> addNewOrderToUserByIsUser(@RequestBody CreateOrder createOrder) throws CustomException {
        Orders orders = iOrderService.addNewOrderToUserByIdUser(createOrder);
        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/admin/listOrder")
    public ResponseEntity<List<ListOrder>> listResponseEntity() {
        return new ResponseEntity<>(iOrderService.getAllOrders(), HttpStatus.OK);
    }
    @PatchMapping("/admin/updateDelivery/{orderId}")
    public  ResponseEntity<String>  updateDelivery(@PathVariable Long orderId) throws CustomException {
             iOrderService.updateOrderDelivery(orderId);
        return ResponseEntity.ok("update order delivery success");
    }

    @PatchMapping("/admin/updateOrder/{orderId}/orderStatus")
    public  ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId,@RequestParam("status") String orderStatus) throws CustomException {
        iOrderService.updateOrderStatus(orderId,orderStatus);
        return ResponseEntity.ok("update order status success");
    }


}
