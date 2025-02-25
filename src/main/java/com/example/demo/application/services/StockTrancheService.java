package com.example.demo.application.services;

import com.example.demo.domain.StockTranche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.adapters.out.StockTrancheRepository;
@Service
public class StockTrancheService {

    @Autowired
    private StockTrancheRepository stockTrancheRepository;

    // Method to save StockTranche
    public StockTranche save(StockTranche stockTranche) {
        return stockTrancheRepository.save(stockTranche);
    }

    // Other methods for StockTranche, like findById(), delete(), etc.
}
