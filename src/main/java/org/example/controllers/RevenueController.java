package org.example.controllers;

import org.example.dtos.CalculateRevenueRequestDTO;
import org.example.dtos.CalculateRevenueResponseDTO;
import org.example.dtos.ResponseStatus;
import org.example.models.AggregatedRevenue;
import org.example.services.RevenueService;

public class RevenueController {
    private final RevenueService revenueService;
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }
    public CalculateRevenueResponseDTO calculateRevenue(CalculateRevenueRequestDTO calculateRevenueRequestDTO) {
        CalculateRevenueResponseDTO calculateRevenueResponseDTO = new CalculateRevenueResponseDTO();
        try {
            AggregatedRevenue aggregatedRevenue = revenueService.calculateRevenue(calculateRevenueRequestDTO.getUserId(), calculateRevenueRequestDTO.getRevenueQueryType());
            calculateRevenueResponseDTO.setAggregatedRevenue(aggregatedRevenue);
            calculateRevenueResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            calculateRevenueResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return calculateRevenueResponseDTO;
    }
}