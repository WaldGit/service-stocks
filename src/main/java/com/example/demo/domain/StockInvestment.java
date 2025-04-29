package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stock_investments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInvestment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // ✅ Correct way to handle UUID in Hibernate 6+
    private UUID id;

    private Double currentPrice;
    private LocalDate currentDatePrice;

    private boolean closed;
    private LocalDate closedDate;

    @Transient
    private InvestmentMetricsDTO metrics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonBackReference
    private Portfolio portfolio;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<StockTranche> tranches = new ArrayList<>(); // ✅ Initialize to avoid NullPointerException

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Override
    public String toString() {
        return "StockInvestment{" +
                "id=" + id +
                ", stock ='" + stock.getName() + '\'' +
                ", currentPrice=" + currentPrice +
                ", closed=" + closed +
                ", closedDate=" + closedDate +
                '}'; // ✅ Removed 'tranches' to prevent LazyInitializationException
    }
}
