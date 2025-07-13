package org.example.dtos;

import org.example.models.Order;

public class PlaceOrderResponseDTO {
    private ResponseStatus responseStatus;
    private Order order;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
