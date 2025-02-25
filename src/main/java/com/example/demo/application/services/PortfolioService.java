package com.example.demo.application.services;

import com.example.demo.adapters.out.PortfolioRepository;
import com.example.demo.domain.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Portfolio> getAllPortfolios() {
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
}

