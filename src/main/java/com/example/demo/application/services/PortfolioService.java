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
}

