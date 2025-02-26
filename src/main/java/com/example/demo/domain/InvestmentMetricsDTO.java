package com.example.demo.domain;

public class InvestmentMetricsDTO {
    private double returnPercentage;
    private double totalShares;
    private double averagePricePerShare;
    private double totalPrice;

    public InvestmentMetricsDTO(double returnPercentage, double totalShares, double averagePricePerShare, double totalPrice) {
        this.returnPercentage = returnPercentage;
        this.totalShares = totalShares;
        this.averagePricePerShare = averagePricePerShare;
        this.totalPrice = totalPrice;
    }

    // Getters and setters

    public double getReturnPercentage() {
        return returnPercentage;
    }

    public void setReturnPercentage(double returnPercentage) {
        this.returnPercentage = returnPercentage;
    }

    public double getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(double totalShares) {
        this.totalShares = totalShares;
    }

    public double getAveragePricePerShare() {
        return averagePricePerShare;
    }

    public void setAveragePricePerShare(double averagePricePerShare) {
        this.averagePricePerShare = averagePricePerShare;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
