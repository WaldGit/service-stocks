package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stock_investments")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class StockInvestment {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // or UUID generator
    private UUID id;

    private String ticker;
    private Double currentPrice;
    private boolean closed;
    private LocalDate closedDate;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)  // Foreign key to Portfolio
    private Portfolio portfolio;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StockTranche> tranches;

    // Getters & Setters


    @Override
    public String toString() {
        return "StockInvestment{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", currentPrice=" + currentPrice +
                ", closed=" + closed +
                ", closedDate=" + closedDate +

                '}';
    }
}
