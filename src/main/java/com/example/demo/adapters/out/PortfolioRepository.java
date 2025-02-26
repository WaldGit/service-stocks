package com.example.demo.adapters.out;

import com.example.demo.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    java.util.Optional<Portfolio> findByName(String name);
}
