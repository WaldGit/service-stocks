package com.example.demo.application.ports.out;

import java.util.UUID;
import com.example.demo.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PortfolioRepository {

    Portfolio findPortfolioWithInvestments(UUID portfolioId);
    Optional<Portfolio> findByName(String name);
    Portfolio save(Portfolio portfolio);
    List<Portfolio> findAll();
    Optional<Portfolio> findById(UUID id);
    void deleteById(UUID id);
}