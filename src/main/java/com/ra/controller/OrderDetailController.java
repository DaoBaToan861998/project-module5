package com.ra.controller;

import com.ra.dto.request.OrderDetailRequest;
import com.ra.dto.response.OrderDetailResponse;
import com.ra.dto.response.OrderResponse;
import com.ra.service.IOrderDetailService;
import com.ra.advice.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class OrderDetailController {
    @Autowired
    private IOrderDetailService iOrderDetailService;
    @PostMapping("/addOrderDetail")
    public ResponseEntity<OrderResponse> addNewOrderDetail(@RequestBody OrderDetailRequest orderDetailRequest) throws CustomException {
        return new ResponseEntity<>(iOrderDetailService.addNewOrderDetail(orderDetailRequest), HttpStatus.OK);
    }

    @GetMapping("/listOrderDetail/{orderId}")
    public  ResponseEntity<List<OrderDetailResponse>> getAllOrderDetailByOrderId(@PathVariable Long orderId) throws CustomException {
        return  new ResponseEntity<>(iOrderDetailService.getAllOrderDetailByOrderId(orderId),HttpStatus.OK);
    }
    @DeleteMapping("/admin/deleteOrder/{orderId}/{orderDetailId}")
    public  ResponseEntity<List<OrderDetailResponse>> deleteOrderDetailByOrderId(@PathVariable Long orderId,@PathVariable Long orderDetailId){
        return new ResponseEntity<>(iOrderDetailService.deleteOrderDetailByOrderId(orderId,orderDetailId),HttpStatus.OK);
    }
}
