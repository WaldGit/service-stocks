package com.example.demo.infrastructure.adapters.in;

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

    @GetMapping("/{id}/metrics")
    public Portfolio getPortfolioWithMetrics(@PathVariable UUID id) {
        System.out.println("TESSTEN");
        return portfolioService.getPortfolioWithMetrics(id);

    }

    // New endpoint to get portfolio by name
    @GetMapping("/name/{portfolioName}")
    public Portfolio getPortfolioByName(@PathVariable String portfolioName) {
        return portfolioService.getPortfolioByName(portfolioName);
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
