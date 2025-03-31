package com.example.demo.adapters.out;

import com.example.demo.domain.StockTranche;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StockTrancheRepository extends JpaRepository<StockTranche, UUID> {
    // Additional custom queries can be added if needed

    // Find all tranches by StockInvestment ID
    java.util.List<StockTranche> findByStock_Id(UUID stocktranchId);
}


