package com.example.demo.application;

import com.example.demo.application.services.PortfolioService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

import com.example.demo.domain.StockInvestment;
import com.example.demo.domain.StockTranche;
import com.example.demo.domain.Portfolio;
import com.example.demo.application.services.StockService;
import com.example.demo.application.services.StockTrancheService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")

public class DemoApplication {

    private static final String TARGET_PORTFOLIO_NAME = "High Yield Portfolio"; // 🔄 Change this to the desired name
    //private static final String TARGET_PORTFOLIO_NAME = "Dividend Growth Stock Portfolio"; // 🔄 Change this to the desired name


    @org.springframework.beans.factory.annotation.Value("classpath:portfolio-high-yield_exported_portfolio.json")
    private org.springframework.core.io.Resource portfolioHighYieldJsonFile;

    @org.springframework.beans.factory.annotation.Value("classpath:portfolio-dividend-growth.json")
    private org.springframework.core.io.Resource portfolioDividendGrowthJsonFile;

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

    private void fetchFromFiles(StockTrancheService stockTrancheService,StockService stockService,PortfolioService  portfolioService,com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        try {

            String jsonContent = readJsonFile(portfolioHighYieldJsonFile);
            this.loadPortfolio(jsonContent,stockService, stockTrancheService, portfolioService, objectMapper);


            String jsonContent2 = readJsonFile(portfolioDividendGrowthJsonFile);
            this.loadPortfolio(jsonContent2,stockService, stockTrancheService, portfolioService, objectMapper);

            System.out.println("🚀 Application started: Searching for portfolio '" + TARGET_PORTFOLIO_NAME + "'");

        } catch (IOException e) {
            System.err.println("❌ Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportPortfolioToJson(PortfolioService portfolioService) {
        Portfolio portfolio = portfolioService.getPortfolioByName(TARGET_PORTFOLIO_NAME);
        System.out.println("Found portfolio: " + portfolio);
        portfolioService.exportPortfolioToJson(portfolio.getId());

    }


    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner run(StockService stockService, StockTrancheService stockTrancheService, PortfolioService portfolioService, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        return args -> {

           //this.fetchFromFiles(portfolioService);
           //this.exportPortfolioToJson(portfolioService);


           this.updatePortfolioMetrics(portfolioService);

        };
    }

    private void updatePortfolioMetrics(PortfolioService portfolioService) {
        Portfolio portfolio = portfolioService.getPortfolioByName(TARGET_PORTFOLIO_NAME);
        System.out.println("Found portfolio: " + portfolio);
        portfolioService.updatePortfolioMetrics(portfolio.getId());
        System.out.println("Done updating portfolio metrics..");

    }

    private void loadPortfolio(String jsonContent, StockService stockService,
                               StockTrancheService stockTrancheService,
                               PortfolioService portfolioService,
                               com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        try {
            // Parse the JSON content to a Portfolio object
            Portfolio portfolio = objectMapper.readValue(jsonContent, Portfolio.class);
            System.out.println("Portfolio : " + portfolio);

            // Process and save the portfolio data to the database
            processAndSavePortfolio(portfolio, stockService, portfolioService, stockTrancheService);

            System.out.println("Portfolio data has been successfully processed and saved.");
            InvestmentReturnCalculator returnCalculator = new InvestmentReturnCalculator();

            // Fetch and print all investments with their tranches
            List<StockInvestment> investments = stockService.getAllStocksInvestements();
            for (StockInvestment inv : investments) {
                System.out.println("📈 Investment: " + inv);

                InvestmentMetrics metrics = returnCalculator.calculateInvestmentMetrics(inv);
                System.out.println("📊 Return for this investment: " + metrics + "%");

                // Iterate over the list of StockTranche objects and print each one
                for (StockTranche tr : inv.getTranches()) {
                    System.out.println("  🏦 Tranche: " + tr);
                }
            }

            System.out.println("Fetching all stocks from DB...");
            //stockService.printAllStocks();

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            System.err.println("❌ Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }






    // Utility method to read the file content as a String
    private String readJsonFile(org.springframework.core.io.Resource resource) throws IOException {
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }


    public void processAndSavePortfolio(
            Portfolio originalPortfolio,
            StockService stockService,
            PortfolioService portfolioService,
            StockTrancheService stockTrancheService) {

        // ✅ Step 1: Clone Portfolio (excluding ID)
        Portfolio clonedPortfolio = new Portfolio();
        clonedPortfolio.setName(originalPortfolio.getName());

        // ✅ Step 2: Save cloned Portfolio FIRST to get a new ID
        clonedPortfolio = portfolioService.save(clonedPortfolio);

        List<StockInvestment> clonedInvestments = new java.util.ArrayList<>();

        // ✅ Step 3: Clone and Save Each StockInvestment
        for (StockInvestment originalInvestment : originalPortfolio.getStockInvestments()) {
            StockInvestment clonedInvestment = new StockInvestment();
            clonedInvestment.setPortfolio(clonedPortfolio); // ✅ Associate with cloned portfolio
            clonedInvestment.setTicker(originalInvestment.getTicker());
            clonedInvestment.setCurrentPrice(originalInvestment.getCurrentPrice());
            clonedInvestment.setClosed(originalInvestment.isClosed());
            clonedInvestment.setClosedDate(originalInvestment.getClosedDate());

            // ✅ Save cloned StockInvestment
            clonedInvestment = stockService.save(clonedInvestment);
            clonedInvestments.add(clonedInvestment);
        }

        // ✅ Step 4: Clone and Save Each StockTranche
        for (int i = 0; i < originalPortfolio.getStockInvestments().size(); i++) {
            StockInvestment originalInvestment = originalPortfolio.getStockInvestments().get(i);
            StockInvestment clonedInvestment = clonedInvestments.get(i);

            for (StockTranche originalTranche : originalInvestment.getTranches()) {
                StockTranche clonedTranche = new StockTranche();
                clonedTranche.setStock(clonedInvestment); // ✅ Associate with cloned StockInvestment
                clonedTranche.setPricePerShare(originalTranche.getPricePerShare());
                clonedTranche.setPurchaseDate(originalTranche.getPurchaseDate());

                // ✅ Calculate new quantity (500 EUR per tranche)
                double quantity = 500.0 / clonedTranche.getPricePerShare();
                clonedTranche.setQuantity(quantity);

                // ✅ Save cloned StockTranche
                stockTrancheService.save(clonedTranche);
            }
        }
    }


}

