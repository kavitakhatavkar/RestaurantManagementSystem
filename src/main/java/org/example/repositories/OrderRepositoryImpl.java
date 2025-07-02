package org.example.repositories;

import org.example.models.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderRepositoryImpl implements OrderRepository {
    private Map<Long, Order> orderMap;
    private static int counter;
    public OrderRepositoryImpl() {
        this.orderMap = new HashMap<>();
        counter = 1;
    }
    @Override
    public Order save(Order order) {
        order.setId(counter++);
        orderMap.put(order.getId(), order);
        return order;
    }
    @Override
    public List<Order> findOrdersByCustomerSession(long customerSessionId) {
        return orderMap.values().stream().filter(order -> order.getCustomerSession().getId() == customerSessionId).collect(Collectors.toList());
    }
}
