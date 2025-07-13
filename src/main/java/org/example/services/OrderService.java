package org.example.services;

import org.example.exceptions.CustomerSessionNotFound;
import org.example.exceptions.InvalidMenuItem;
import org.example.exceptions.UserNotFoundException;
import org.example.models.Bill;
import org.example.models.Order;

import java.util.Map;

public interface OrderService {
    public Bill generateBill(long userId) throws CustomerSessionNotFound;
    public Order placeOrder(long userId, Map<Long, Integer> orderedItems) throws UserNotFoundException, CustomerSessionNotFound, InvalidMenuItem;
}
