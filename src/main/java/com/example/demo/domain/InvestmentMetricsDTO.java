package com.example.demo.domain;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class InvestmentMetricsDTO {
    private double returnPercentage;
    private double totalShares;
    private double averagePricePerShare;
    private double totalPrice;
    private double totalGainLoss;
    private double allocationPercentage;
}
