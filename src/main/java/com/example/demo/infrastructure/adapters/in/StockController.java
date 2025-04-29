package com.example.demo.infrastructure.adapters.in;

import com.example.demo.adapters.out.persistence.StockRepository;
import com.example.demo.application.ports.out.PortfolioRepository;
import com.example.demo.domain.StockInvestment;
import com.example.demo.application.services.StockService; // Add this import statemen
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocksInvestments")
public class StockController {
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final com.example.demo.application.ports.out.PortfolioRepository portfolioRepository;

    public StockController(StockService stockService, PortfolioRepository PortfolioRepository, StockRepository stockRepository) {
        this.stockService = stockService;
        this.portfolioRepository = PortfolioRepository;
        this.stockRepository = stockRepository;
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

        com.example.demo.domain.Stock stock = stockRepository.save(investment.getStock()); // save Stock first
        investment.setStock(stock);


        // Save the investment
        StockInvestment savedInvestment = stockService.save(investment);
        return org.springframework.http.ResponseEntity.ok(savedInvestment);
    }




}
