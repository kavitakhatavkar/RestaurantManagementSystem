package org.example.controllers;

import org.example.dtos.*;
import org.example.models.Bill;
import org.example.models.Order;
import org.example.services.OrderService;

public class OrderController {
    private OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    public GenerateBillResponseDTO generateBill(GenerateBillRequestDTO generateBillRequestDTO) {
        GenerateBillResponseDTO generateBillResponseDTO = new GenerateBillResponseDTO();
        try {
            Bill bill = orderService.generateBill(generateBillRequestDTO.getUserId());
            generateBillResponseDTO.setBill(bill);
            generateBillResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            generateBillResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return generateBillResponseDTO;
    }

    public PlaceOrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO) {
        PlaceOrderResponseDTO placeOrderResponseDTO = new PlaceOrderResponseDTO();
        try {
            Order order = orderService.placeOrder(placeOrderRequestDTO.getUserId(), placeOrderRequestDTO.getOrderedItems());
            placeOrderResponseDTO.setOrder(order);
            placeOrderResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e){
            placeOrderResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return placeOrderResponseDTO;
    }
}
