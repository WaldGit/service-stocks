package com.example.demo.domain;

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

    private String ticker;
    private Double currentPrice;
    private boolean closed;
    private LocalDate closedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    private Portfolio portfolio;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<StockTranche> tranches = new ArrayList<>(); // ✅ Initialize to avoid NullPointerException

    @Override
    public String toString() {
        return "StockInvestment{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", currentPrice=" + currentPrice +
                ", closed=" + closed +
                ", closedDate=" + closedDate +
                '}'; // ✅ Removed 'tranches' to prevent LazyInitializationException
    }
}
