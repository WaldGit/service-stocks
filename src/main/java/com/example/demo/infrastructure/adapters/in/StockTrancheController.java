package com.example.demo.infrastructure.adapters.in;

import com.example.demo.adapters.out.persistence.StockInvestmentRepository;
import com.example.demo.application.services.StockTrancheService;
import com.example.demo.domain.StockInvestment;
import com.example.demo.domain.StockTranche;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.StockTrancheDTO;
import com.example.demo.adapters.out.persistence.StockTrancheRepository;

@Slf4j
@RestController
@RequestMapping("/api/stock-tranches")
public class StockTrancheController {

    private final StockTrancheService stockTrancheService;
    private final StockInvestmentRepository stockInvestmentRepository;
    private final StockTrancheRepository stockTrancheRepository;

    @Autowired
    public StockTrancheController(StockTrancheService stockTrancheService,StockInvestmentRepository stockInvestmentRepository,StockTrancheRepository stockTrancheRepository) {
        this.stockTrancheService = stockTrancheService;
        this.stockInvestmentRepository = stockInvestmentRepository;
        this.stockTrancheRepository = stockTrancheRepository;
    }



    // Endpoint to save a new StockTranche
    @PostMapping
    public ResponseEntity<StockTranche> createTranche(@RequestBody StockTrancheDTO trancheDTO) {
        StockInvestment stock = stockInvestmentRepository.findById(trancheDTO.getStock().getId())
                .orElseThrow(() -> new RuntimeException("Stock Investment not found"));

        StockTranche tranche = StockTranche.builder()
                .pricePerShare(trancheDTO.getPricePerShare())
                .quantity(trancheDTO.getQuantity())
                .purchaseDate(trancheDTO.getPurchaseDate())
                .stock(stock)
                .build();

        System.out.println("TRANCHE " + tranche);

        StockTranche savedTranche = stockTrancheRepository.save(tranche);
        return ResponseEntity.ok(savedTranche);
    }


    // Other endpoints for StockTranche can be added here (e.g., get by ID, delete, etc.)
}
