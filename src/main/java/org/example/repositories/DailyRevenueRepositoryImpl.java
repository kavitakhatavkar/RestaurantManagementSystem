package org.example.repositories;

import org.example.models.DailyRevenue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DailyRevenueRepositoryImpl implements DailyRevenueRepository {
    private List<DailyRevenue> dailyRevenues;
    public DailyRevenueRepositoryImpl() {
        this.dailyRevenues = new ArrayList<>();
    }
    @Override
    public DailyRevenue save(DailyRevenue dailyRevenue) {
        dailyRevenues.add(dailyRevenue);
        return dailyRevenue;
    }
    @Override
    public List<DailyRevenue> getDailyRevenueBetweenDates(Date startDate, Date endDate) {
        return dailyRevenues.stream().
                filter(dailyRevenue -> dailyRevenue.getDate().compareTo(startDate) >= 0
                        && dailyRevenue.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
    }
}
