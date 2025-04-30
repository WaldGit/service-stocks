package com.example.demo.adapters.out.persistence;

import com.example.demo.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    java.util.Optional<Stock> findByName(String name);
}
