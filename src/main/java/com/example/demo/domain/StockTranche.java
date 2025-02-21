package com.example.demo.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "stock_tranches")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
@lombok.Setter
@lombok.Getter
public class StockTranche {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;  // Each tranche needs a unique identifier

    private double pricePerShare;
    private double quantity;
    private LocalDate purchaseDate;
    private Double transactionCost;
    private Double percentageGain;

    @ManyToOne
    @JoinColumn(name = "stocktranch_id", nullable = false) // ✅ This correctly maps to StockInvestment
    private StockInvestment stock;

    // Getters and Setters


    @Override
    public String toString() {
        return "StockTranche{" +
                "id=" + id +
                ", pricePerShare=" + pricePerShare +
                ", quantity=" + quantity +
                ", purchaseDate=" + purchaseDate +
                ", transactionCost=" + transactionCost +
                ", percentageGain=" + percentageGain +
                ", stock=" + stock +
                '}';
    }
}
