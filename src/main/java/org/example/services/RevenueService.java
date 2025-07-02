package org.example.services;

import org.example.exceptions.UnAuthorizedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.AggregatedRevenue;

public interface RevenueService {
    public AggregatedRevenue calculateRevenue(long userId, String queryType) throws UnAuthorizedAccess, UserNotFoundException;
}
