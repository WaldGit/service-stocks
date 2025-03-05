package com.example.demo.dto;

import com.example.demo.domain.StockInvestment;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTrancheDTO {
    private UUID id;  // Optional, used for updates
    private double pricePerShare;
    private double quantity;
    private LocalDate purchaseDate;
    private Double transactionCost;
    private Double percentageGain;
    private StockInvestment stock; // Only the StockInvestment ID, not the full object
}
