package com.example.demo.application;

public class InvestmentReturnCalculator {

    // Method to calculate the return for a StockInvestment based on its tranches
    public InvestmentMetrics calculateInvestmentMetrics(com.example.demo.domain.StockInvestment investment) {
        double totalGain = 0.0;
        double totalInvestment = 0.0;
        double totalShares = 0.0;  // Total number of shares across all tranches
        double totalPrice = 0.0;   // Total price across all tranches (price per share * quantity)

        // Iterate over all tranches of the investment
        for (com.example.demo.domain.StockTranche tranche : investment.getTranches()) {
            // Calculate gain for each tranche: (current price - price per share) * quantity
            double gain = (investment.getCurrentPrice() - tranche.getPricePerShare()) * tranche.getQuantity();
            totalGain += gain;

            // Calculate the total amount invested for this tranche: price per share * quantity
            totalInvestment += tranche.getPricePerShare() * tranche.getQuantity();

            // Calculate total number of shares
            totalShares += tranche.getQuantity();

            // Calculate the total price paid for all shares in this tranche
            totalPrice += tranche.getPricePerShare() * tranche.getQuantity();

            // Calculate the percentage gain for this tranche and set it
            double percentageGain = ((investment.getCurrentPrice() - tranche.getPricePerShare()) / tranche.getPricePerShare()) * 100;
            tranche.setPercentageGain(percentageGain);  // Set the percentage gain on the tranche
        }

        // If total investment is 0 (to avoid division by zero), return 0% return
        if (totalInvestment == 0) {
            return new InvestmentMetrics(0.0, totalShares, 0.0, 0.0);
        }

        // Calculate the overall return as a percentage
        double returnPercentage = (totalGain / totalInvestment) * 100;

        // Calculate the average price per share
        double averagePricePerShare = totalPrice / totalShares;

        // Return all metrics as an object
        return new InvestmentMetrics(returnPercentage, totalShares, averagePricePerShare, totalPrice);
    }
}
