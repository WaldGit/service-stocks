package com.example.demo.application;

import com.example.demo.application.services.PortfolioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.domain.StockInvestment;
import com.example.demo.domain.StockTranche;
import com.example.demo.domain.Portfolio;
import com.example.demo.application.services.StockService;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")

public class DemoApplication {



	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	  @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner run(StockService stockService, PortfolioService portfolioService) {
        return args -> {

            // 1. Create a Portfolio
            Portfolio portfolio = Portfolio.builder()
                    .name("Tech Stocks Portfolio")
                    .build();

// Save the Portfolio
            portfolio = portfolioService.save(portfolio);
            System.out.println("✅ Created Portfolio: " + portfolio);

// 2. Create a StockInvestment
            StockInvestment investment = StockInvestment.builder()
                    .ticker("AAPL")
                    .currentPrice(175.50)
                    .closed(false)
                    .closedDate(null)
                    .portfolio(portfolio) // Associate this investment with the portfolio
                    .build();

// Save the StockInvestment
            investment = stockService.save(investment);
            System.out.println("✅ Created Investment: " + investment);

// 3. Create StockTranches for this investment
            StockTranche tranche1 = StockTranche.builder()
                    .quantity(10.0)
                    .pricePerShare(170.00)
                    .purchaseDate(LocalDate.now().minusDays(10))
                    .stock(investment)  // Associate this tranche with the investment
                    .build();

            StockTranche tranche2 = StockTranche.builder()
                    .quantity(20.0)
                    .pricePerShare(172.00)
                    .purchaseDate(LocalDate.now().minusDays(5))
                    .stock(investment)  // Associate this tranche with the investment
                    .build();

// Save all tranches
            stockService.saveAll(List.of(tranche1, tranche2));
            System.out.println("✅ Added Tranches: " + tranche1 + ", " + tranche2);

// 4. Fetch and print all investments with their tranches
            List<StockInvestment> investments = stockService.getAllStocksInvestements();
            for (StockInvestment inv : investments) {
                System.out.println("📈 Investment: " + inv);

                // Access the list of tranches for this investment
                java.util.List<StockTranche> tranches = inv.getTranches();

                // Iterate over the list of StockTranche objects and print each one
                for (StockTranche tr : tranches) {
                    System.out.println("  🏦 Tranche: " + tr);
                }
            }

            System.out.println("Fetching all stocks from DB...");
            //stockService.printAllStocks();
        };
    }

}
