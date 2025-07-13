package org.example.services;

import org.example.exceptions.CustomerSessionNotFound;
import org.example.exceptions.InvalidMenuItem;
import org.example.exceptions.UserNotFoundException;
import org.example.models.*;
import org.example.repositories.CustomerSessionRepository;
import org.example.repositories.MenuItemRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;
    private OrderRepository orderRepository;
    private CustomerSessionRepository customerSessionRepository;

    public OrderServiceImpl(UserRepository userRepository, MenuItemRepository menuItemRepository, CustomerSessionRepository customerSessionRepository, OrderRepository orderRepository) {
        this.customerSessionRepository = customerSessionRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public OrderServiceImpl(OrderRepository orderRepository, CustomerSessionRepository customerSessionRepository) {
        this.orderRepository = orderRepository;
        this.customerSessionRepository = customerSessionRepository;
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

    @Override
    public Order placeOrder(long userId, Map<Long, Integer> orderedItems) throws UserNotFoundException, InvalidMenuItem {
        Optional<CustomerSession> customerSessionOptional = customerSessionRepository.findActiveCustomerSessionByUserId(userId);
        CustomerSession customerSession;
        if (customerSessionOptional.isEmpty()) {
            customerSession = new CustomerSession();
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("User not found");
            }
            customerSession.setUser(userOptional.get());
            customerSession.setCustomerSessionStatus(CustomerSessionStatus.ACTIVE);
            customerSession = customerSessionRepository.save(customerSession);
        } else {
            customerSession = customerSessionOptional.get();
        }
        Order order = new Order();
        order.setCustomerSession(customerSession);
        Map<MenuItem, Integer> menuItemQuantityMap = new HashMap<>();
        for(Map.Entry<Long, Integer> entry: orderedItems.entrySet()){
            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(entry.getKey());
            if(optionalMenuItem.isPresent()){
                menuItemQuantityMap.put(optionalMenuItem.get(), entry.getValue());
            } else {
                throw new InvalidMenuItem("Menu item not found");
            }
        }
        order.setOrderedItems(menuItemQuantityMap);
        order = orderRepository.save(order);
        return order;
    }
}