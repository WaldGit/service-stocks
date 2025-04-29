package com.example.demo.application.services;
import com.example.demo.adapters.out.persistence.StockTrancheRepository;
import com.example.demo.adapters.out.persistence.StockInvestmentRepository;
import com.example.demo.domain.StockInvestment;
import com.example.demo.domain.StockTranche;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {
    @org.springframework.beans.factory.annotation.Autowired
    private final StockInvestmentRepository stockInvestmentRepository = null;

    @org.springframework.beans.factory.annotation.Autowired
    private StockTrancheRepository stockTrancheRepository;

    // Save or update a StockInvestment
    public StockInvestment save(StockInvestment stockInvestment) {
        return stockInvestmentRepository.save(stockInvestment);
    }

    public List<StockTranche> saveAll(List<StockTranche> stockTranches) {
        return stockTrancheRepository.saveAll(stockTranches);
    }

    public List<StockInvestment> getAllStocksInvestements() {
        return stockInvestmentRepository.findAll();
    }



    // Create or update a StockInvestment
    public StockInvestment saveStockInvestment(StockInvestment stockInvestment) {
        return stockInvestmentRepository.save(stockInvestment);
    }

    // Get a StockInvestment by its ID
    public Optional<StockInvestment> getStockInvestmentById(UUID id) {
        return stockInvestmentRepository.findById(id);
    }

    // Get all StockInvestments
    public List<StockInvestment> getAllStockInvestments() {
        return stockInvestmentRepository.findAll();
    }

    // Delete a StockInvestment
    public void deleteStockInvestment(UUID id) {
        stockInvestmentRepository.deleteById(id);
    }

    public StockInvestment findByTicker(String ticker) {
        // Use the repository to find a stock by its ticker
        return stockInvestmentRepository.findByTicker(ticker)
                .orElseThrow(() -> new IllegalArgumentException("Stock with ticker " + ticker + " not found."));
    }


}
