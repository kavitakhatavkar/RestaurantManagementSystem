package org.example.dtos;

import java.util.Map;

public class PlaceOrderRequestDTO {
    private long userId;
    private Map<Long,Integer> orderedItems;
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public Map<Long, Integer> getOrderedItems() {
        return orderedItems;
    }
    public void setOrderedItems(Map<Long, Integer> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
