package org.example.services;

import org.example.exceptions.UnAuthorizedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.*;
import org.example.repositories.DailyRevenueRepository;
import org.example.repositories.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RevenueServiceImpl implements RevenueService {
    private DailyRevenueRepository dailyRevenueRepository;
    private UserRepository userRepository;
    public RevenueServiceImpl(DailyRevenueRepository dailyRevenueRepository, UserRepository userRepository) {
        this.dailyRevenueRepository = dailyRevenueRepository;
        this.userRepository = userRepository;
    }
    @Override
    public AggregatedRevenue calculateRevenue(long userId, String queryType) throws UnAuthorizedAccess, UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();
        if (!user.getUserType().equals(UserType.BILLING)) {
            throw new UnAuthorizedAccess("User not authorized");
        }
        RevenueQueryType revenueQueryType = RevenueQueryType.valueOf(queryType);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate;
        Date endDate;

        if (revenueQueryType.equals(RevenueQueryType.CURRENT_MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = calendar.getTime();
        } else if(revenueQueryType.equals(RevenueQueryType.CURRENT_FY)){
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            endDate = calendar.getTime();

        } else if(revenueQueryType.equals(RevenueQueryType.PREVIOUS_MONTH)){
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDate = calendar.getTime();
        } else {
            calendar.add(Calendar.YEAR, -1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            endDate = calendar.getTime();
        }
        List<DailyRevenue> dailyRevenues = dailyRevenueRepository.getDailyRevenueBetweenDates(startDate, endDate);

        // sum all the revenue
        double totalGST = 0;
        double totalServiceCharge = 0;
        double revenueFromFoodSales = 0;

        for (DailyRevenue dailyRevenue : dailyRevenues) {
            totalGST += dailyRevenue.getTotalGst();
            totalServiceCharge += dailyRevenue.getTotalServiceCharge();
            revenueFromFoodSales += dailyRevenue.getRevenueFromFoodSales();
        }

        // set aggregate revenue
        AggregatedRevenue aggregatedRevenue = new AggregatedRevenue();
        aggregatedRevenue.setFromDate(startDate);
        aggregatedRevenue.setToDate(endDate);
        aggregatedRevenue.setTotalGst(totalGST);
        aggregatedRevenue.setTotalServiceCharge(totalServiceCharge);
        aggregatedRevenue.setRevenueFromFoodSales(revenueFromFoodSales);
        aggregatedRevenue.setTotalRevenue(totalGST + totalServiceCharge + revenueFromFoodSales);
        return aggregatedRevenue;
    }
}
