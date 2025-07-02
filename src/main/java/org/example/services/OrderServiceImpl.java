package org.example.services;

import org.example.exceptions.CustomerSessionNotFound;
import org.example.models.*;
import org.example.repositories.CustomerSessionRepository;
import org.example.repositories.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private CustomerSessionRepository customerSessionRepository;
    private OrderRepository orderRepository;
    public OrderServiceImpl(CustomerSessionRepository customerSessionRepository, OrderRepository orderRepository) {
        this.customerSessionRepository = customerSessionRepository;
        this.orderRepository = orderRepository;
    }
    @Override
    public Bill generateBill(long userId) throws CustomerSessionNotFound {
        Optional<CustomerSession> customerSessionOptional = customerSessionRepository.findActiveCustomerSessionByUserId(userId);
        if (customerSessionOptional.isEmpty()) {
            throw new CustomerSessionNotFound("Customer session not found");
        }
        CustomerSession customerSession = customerSessionOptional.get();
        customerSession.setCustomerSessionStatus(CustomerSessionStatus.ENDED);
        customerSessionRepository.save(customerSession);

        List<Order> orders = orderRepository.findOrdersByCustomerSession(customerSession.getId());
        Map<MenuItem, Integer> consolidatedOrderedItem = new HashMap<>();
        for (Order order : orders) {
            for (Map.Entry<MenuItem, Integer> entry : order.getOrderedItems().entrySet()) {
                consolidatedOrderedItem.put(entry.getKey(), consolidatedOrderedItem.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        double totalAmount = 0;
        for (Map.Entry<MenuItem, Integer> entry : consolidatedOrderedItem.entrySet()) {
            totalAmount += entry.getKey().getPrice() * entry.getValue();
        }
        double gst = totalAmount * 0.05;
        double serviceCharge = totalAmount * 0.1;
        double finalTotal = totalAmount + gst + serviceCharge;

        Bill bill = new Bill();
        bill.setGst(gst);
        bill.setServiceCharge(serviceCharge);
        bill.setTotalAmount(finalTotal);
        bill.setOrderedItems(consolidatedOrderedItem);
        return bill;
    }
}
