package org.example.dtos;

public class CalculateRevenueRequestDTO {
    private String revenueQueryType;
    private long userId;

    public String getRevenueQueryType() {
        return revenueQueryType;
    }

    public void setRevenueQueryType(String revenueQueryType) {
        this.revenueQueryType = revenueQueryType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
