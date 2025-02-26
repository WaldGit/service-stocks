package com.example.demo.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "portfolios")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
@lombok.Setter
@lombok.Getter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Transient
    private Double totalGainLoss;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockInvestment> stockInvestments = new ArrayList<>();

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stockInvestments=" + stockInvestments +
                '}';
    }
}
