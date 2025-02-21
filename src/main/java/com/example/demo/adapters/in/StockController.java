package com.example.demo.adapters.in;
import com.example.demo.domain.StockInvestment;
import com.example.demo.application.services.StockService; // Add this import statemen
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }






}
