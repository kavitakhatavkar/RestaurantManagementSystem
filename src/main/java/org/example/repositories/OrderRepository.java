package org.example.repositories;

import org.example.models.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);
    List<Order> findOrdersByCustomerSession(long customerSessionId);
}
