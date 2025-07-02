package org.example.services;

import org.example.exceptions.CustomerSessionNotFound;
import org.example.models.Bill;

public interface OrderService {
    public Bill generateBill(long userId) throws CustomerSessionNotFound;
}
