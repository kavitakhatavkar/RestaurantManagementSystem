package org.example.repositories;

import org.example.models.CustomerSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerSessionRepositoryImpl implements CustomerSessionRepository {
    Map<Long, CustomerSession> customerSessionMap;
    private static int counter;
    public CustomerSessionRepositoryImpl() {
        this.customerSessionMap = new HashMap<>();
        counter = 1;
    }

    @Override
    public CustomerSession save(CustomerSession customerSession) {
        customerSession.setId(counter++);
        customerSessionMap.put(customerSession.getId(), customerSession);
        return customerSession;
    }

    @Override
    public Optional<CustomerSession> findActiveCustomerSessionByUserId(long userId) {
        for (Long key : customerSessionMap.keySet()) {
            if (customerSessionMap.get(key).getUser().getId() == userId) {
                return Optional.of(customerSessionMap.get(key));
            }
        }
        return Optional.empty();
    }
}
