
package com.example.demo.adapters.out.persistence;

import com.example.demo.domain.StockInvestment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockInvestmentRepository extends JpaRepository<StockInvestment, UUID> {
        // Additional custom queries can be added if needed
        //java.util.Optional<StockInvestment> findByTicker(String ticker);

                 List<StockInvestment> findByPortfolio_Id(UUID portfolioId);

        boolean existsByPortfolioAndStock(com.example.demo.domain.Portfolio portfolio, com.example.demo.domain.Stock stock);

}

