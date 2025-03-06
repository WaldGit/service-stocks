package com.example.demo.adapters.in;
import com.example.demo.domain.StockInvestment;
import com.example.demo.application.services.StockService; // Add this import statemen
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocksInvestments")
public class StockController {
    private final StockService stockService;
    private final com.example.demo.adapters.out.PortfolioRepository portfolioRepository;

    public StockController(StockService stockService, com.example.demo.adapters.out.PortfolioRepository PortfolioRepository) {
        this.stockService = stockService;
        this.portfolioRepository = PortfolioRepository;
    }



    @PostMapping
    public org.springframework.http.ResponseEntity<StockInvestment> addInvestment(@RequestBody StockInvestment investment) {
        System.out.println("Saving investment: " + investment);

        if (investment.getPortfolio() == null || investment.getPortfolio().getId() == null) {
            return org.springframework.http.ResponseEntity.badRequest().body(null); // Returns error if portfolio is not set
        }

        // Fetch the Portfolio from DB to ensure it exists
        com.example.demo.domain.Portfolio portfolio = portfolioRepository.findById(investment.getPortfolio().getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        // Set the portfolio for the investment before saving
        investment.setPortfolio(portfolio);

        // Save the investment
        StockInvestment savedInvestment = stockService.save(investment);
        return org.springframework.http.ResponseEntity.ok(savedInvestment);
    }




}
