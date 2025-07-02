package org.example.dtos;

import org.example.models.AggregatedRevenue;

public class CalculateRevenueResponseDTO {
    private AggregatedRevenue aggregatedRevenue;
    private ResponseStatus responseStatus;

    public AggregatedRevenue getAggregatedRevenue() {
        return aggregatedRevenue;
    }

    public void setAggregatedRevenue(AggregatedRevenue aggregatedRevenue) {
        this.aggregatedRevenue = aggregatedRevenue;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
