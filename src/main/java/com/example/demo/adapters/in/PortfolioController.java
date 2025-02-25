package com.example.demo.adapters.in;

import com.example.demo.application.services.PortfolioService;
import com.example.demo.domain.Portfolio;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolios") // Base URL for Portfolio API
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    // ✅ Get all portfolios
    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioService.findAll();
    }

    // ✅ Get portfolio by ID
    @GetMapping("/{id}")
    public Portfolio getPortfolioById(@PathVariable UUID id) {
        return portfolioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    // ✅ Create a new portfolio
    @PostMapping
    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
        return portfolioService.save(portfolio);
    }


}
