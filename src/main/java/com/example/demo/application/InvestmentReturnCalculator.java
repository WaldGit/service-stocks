package com.example.demo.application;

public class InvestmentReturnCalculator {

    // Method to calculate the return for a StockInvestment based on its tranches
    public double calculateInvestmentReturn(com.example.demo.domain.StockInvestment investment) {
        double totalGain = 0.0;
        double totalInvestment = 0.0;

        // Iterate over all tranches of the investment
        for (com.example.demo.domain.StockTranche tranche : investment.getTranches()) {
            // Calculate gain for each tranche: (current price - price per share) * quantity
            double gain = (investment.getCurrentPrice() - tranche.getPricePerShare()) * tranche.getQuantity();
            totalGain += gain;

            // Calculate the total amount invested for this tranche: price per share * quantity
            totalInvestment += tranche.getPricePerShare() * tranche.getQuantity();
        }

        // If total investment is 0 (to avoid division by zero), return 0% return
        if (totalInvestment == 0) {
            return 0.0;
        }

        // Return the overall return as a percentage
        return (totalGain / totalInvestment) * 100;
    }
}
