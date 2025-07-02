package org.example.controllers;

import org.example.dtos.GenerateBillRequestDTO;
import org.example.dtos.GenerateBillResponseDTO;
import org.example.dtos.ResponseStatus;
import org.example.models.Bill;
import org.example.services.OrderService;

public class OrderController {
    private final OrderService orderService;
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
}
