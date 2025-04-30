package com.example.demo.infrastructure.adapters.in;

import com.example.demo.adapters.out.persistence.StockRepository;
import com.example.demo.application.ports.out.PortfolioRepository;
import com.example.demo.domain.Stock;
import com.example.demo.domain.StockInvestment;
import com.example.demo.application.services.StockService; // Add this import statemen
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.Portfolio;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stocksInvestments")
public class StockController {
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final com.example.demo.application.ports.out.PortfolioRepository portfolioRepository;
    private final com.example.demo.adapters.out.persistence.StockInvestmentRepository stockInvestmentRepository;
    public StockController(StockService stockService, PortfolioRepository PortfolioRepository, StockRepository stockRepository, com.example.demo.adapters.out.persistence.StockInvestmentRepository stockInvestmentRepository) {
        this.stockService = stockService;
        this.portfolioRepository = PortfolioRepository;
        this.stockRepository = stockRepository;
        this.stockInvestmentRepository = stockInvestmentRepository;
    }



    @PostMapping
    public ResponseEntity<?> addInvestment(@RequestBody StockInvestment investment) {
        System.out.println("Saving investment: " + investment);

        if (investment.getPortfolio() == null || investment.getPortfolio().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Portfolio must be provided
        }

        // Ensure the portfolio exists
        Portfolio portfolio = portfolioRepository.findById(investment.getPortfolio().getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        investment.setPortfolio(portfolio);

        // Find or create the stock (based on unique identifier like name)
        // Find or create the stock
        String stockName = investment.getStock().getName();
        Stock stock = stockRepository.findByName(stockName)
                .orElseGet(() -> stockRepository.save(investment.getStock()));

        // Check if a StockInvestment already exists for this portfolio and stock
        boolean exists = stockInvestmentRepository.existsByPortfolioAndStock(portfolio, stock);
        if (exists) {

            Map<String, String> error = new HashMap<>();
            error.put("error", "StockInvestment already exists");
            error.put("details", "A StockInvestment already exists for stock '" + stockName + "' in the selected portfolio.");

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(error);
        }

        investment.setStock(stock);
        StockInvestment savedInvestment = stockService.save(investment);
        return ResponseEntity.ok(savedInvestment);
    }








}
