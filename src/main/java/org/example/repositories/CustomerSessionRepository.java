package org.example.repositories;

import org.example.models.CustomerSession;

import java.util.Optional;

public interface CustomerSessionRepository {
    public CustomerSession save(CustomerSession customerSession);
    public Optional<CustomerSession> findActiveCustomerSessionByUserId(long userId);
}
