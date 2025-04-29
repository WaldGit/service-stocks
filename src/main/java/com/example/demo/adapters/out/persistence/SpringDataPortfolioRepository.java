package com.example.demo.adapters.out.persistence;

import com.example.demo.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Query("SELECT p FROM Portfolio p LEFT JOIN FETCH p.stockInvestments WHERE p.id = :portfolioId")
    Portfolio findPortfolioWithInvestments(UUID portfolioId);

    @EntityGraph(attributePaths = {"stockInvestments"})
    Optional<Portfolio> findByName(String name);
}
