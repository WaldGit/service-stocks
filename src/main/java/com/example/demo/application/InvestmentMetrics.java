package com.example.demo.application;

public class InvestmentMetrics {

    private final double returnPercentage;
    private final double totalShares;
    private final double averagePricePerShare;
    private final double totalPrice;

    // Constructor
    public InvestmentMetrics(double returnPercentage, double totalShares, double averagePricePerShare, double totalPrice) {
        this.returnPercentage = returnPercentage;
        this.totalShares = totalShares;
        this.averagePricePerShare = averagePricePerShare;
        this.totalPrice = totalPrice;
    }

    // Getters
    public double getReturnPercentage() {
        return returnPercentage;
    }

    public double getTotalShares() {
        return totalShares;
    }

    public double getAveragePricePerShare() {
        return averagePricePerShare;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "InvestmentMetrics{" +
                "returnPercentage=" + returnPercentage +
                "%, totalShares=" + totalShares +
                ", averagePricePerShare=" + averagePricePerShare +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
