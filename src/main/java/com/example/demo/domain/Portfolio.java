package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "portfolios")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockInvestment> stockInvestments;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StockInvestment> getStockInvestments() {
        return stockInvestments;
    }

    public void setStockInvestments(List<StockInvestment> stockInvestments) {
        this.stockInvestments = stockInvestments;
    }
}
