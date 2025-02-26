package com.example.demo.application.services;

import com.example.demo.adapters.out.PortfolioRepository;
import com.example.demo.domain.InvestmentMetricsDTO;
import com.example.demo.domain.Portfolio;
import com.example.demo.domain.StockInvestment;
import com.example.demo.domain.StockTranche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    public Portfolio getPortfolioById(UUID id) {
        return portfolioRepository.findById(id).orElse(null);
    }

    public void deletePortfolio(UUID id) {
        portfolioRepository.deleteById(id);
    }

    public void processAndSavePortfolio(Portfolio portfolio) {
        // Define the amount to invest in each tranche (500 EUR in this example)
        double totalInvestmentAmount = 500;

        // Loop through each stock investment
        for (com.example.demo.domain.StockInvestment stockInvestment : portfolio.getStockInvestments()) {
            // Loop through each tranche
            for (com.example.demo.domain.StockTranche tranche : stockInvestment.getTranches()) {
                // Calculate the quantity for each tranche
                int quantity = (int) (totalInvestmentAmount / tranche.getPricePerShare());
                tranche.setQuantity(quantity); // Set the calculated quantity
            }
        }

        // Save the portfolio, stock investments, and tranches to the database
        portfolioRepository.save(portfolio);
    }

    public Optional<Portfolio> findById(UUID id) {
        return portfolioRepository.findById(id);
    }

    // New method to get Portfolio by name
    public Portfolio getPortfolioByName(String name) {
        // Find the portfolio by name (assuming portfolioRepository has this method)
        java.util.Optional<Portfolio> portfolioOptional = portfolioRepository.findByName(name);
        return portfolioOptional.orElse(null);  // return null if not found
    }

    public Portfolio getPortfolioWithMetrics(UUID portfolioId) {
        // Fetch the portfolio from the repository
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        double totalPortfolioGain = 0.0;

        // Iterate through all the stock investments and calculate metrics for each
        for (StockInvestment investment : portfolio.getStockInvestments()) {
            InvestmentMetricsDTO metrics = calculateInvestmentMetrics(investment);
            investment.setMetrics(metrics); // Store metrics on the investment (if needed)
            totalPortfolioGain += metrics.getTotalGainLoss();
        }

        portfolio.setTotalGainLoss(totalPortfolioGain); // Store total gain/loss in portfolio


        System.out.println("-----------------------");
        System.out.println(portfolio.getTotalGainLoss());

        // Return portfolio with investment metrics included
        return portfolio;
    }

    public InvestmentMetricsDTO calculateInvestmentMetrics(StockInvestment investment) {
        double totalGainLoss = 0.0;
        double totalInvestment = 0.0;
        double totalShares = 0.0;
        double totalPrice = 0.0;

        for (StockTranche tranche : investment.getTranches()) {
            // Calculate gain/loss for the tranche
            double gainLoss = (investment.getCurrentPrice() - tranche.getPricePerShare()) * tranche.getQuantity();
            totalGainLoss += gainLoss;

            // Total investment amount
            totalInvestment += tranche.getPricePerShare() * tranche.getQuantity();

            // Total number of shares
            totalShares += tranche.getQuantity();

            // Total price paid for all shares
            totalPrice += tranche.getPricePerShare() * tranche.getQuantity();

            // Calculate percentage gain for the tranche
            double percentageGain = ((investment.getCurrentPrice() - tranche.getPricePerShare()) / tranche.getPricePerShare()) * 100;
            tranche.setPercentageGain(percentageGain);
        }

        // Avoid division by zero
        double returnPercentage = (totalInvestment == 0) ? 0.0 : (totalGainLoss / totalInvestment) * 100;
        double averagePricePerShare = (totalShares == 0) ? 0.0 : totalPrice / totalShares;

        // Return the updated metrics
        return new InvestmentMetricsDTO(returnPercentage, totalShares, averagePricePerShare, totalPrice, totalGainLoss);
    }


}

