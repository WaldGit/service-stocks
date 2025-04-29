package com.example.demo.application.tools;

import com.example.demo.application.InvestmentReturnCalculator;
import com.example.demo.application.services.PortfolioService;
import com.example.demo.application.services.StockService;
import com.example.demo.application.services.StockTrancheService;
import com.example.demo.domain.StockTranche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.example.demo.domain.Portfolio;
import com.example.demo.domain.StockInvestment;
import com.example.demo.application.InvestmentMetrics;
import com.example.demo.domain.StockTranche;


public class ExportIn
{
    @org.springframework.beans.factory.annotation.Value("classpath:portfolio-high-yield_exported_portfolio.json")
    private static org.springframework.core.io.Resource portfolioHighYieldJsonFile;

    @org.springframework.beans.factory.annotation.Value("classpath:portfolio-dividend-growth.json")
    private static org.springframework.core.io.Resource portfolioDividendGrowthJsonFile;

    private static void importFetchFromFiles(StockTrancheService stockTrancheService,StockService stockService,PortfolioService  portfolioService,com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        try {

            String jsonContent = readJsonFile(portfolioHighYieldJsonFile);
            loadPortfolio(jsonContent,stockService, stockTrancheService, portfolioService, objectMapper);


            String jsonContent2 = readJsonFile(portfolioDividendGrowthJsonFile);
            loadPortfolio(jsonContent2,stockService, stockTrancheService, portfolioService, objectMapper);

            System.out.println("🚀All json are imported into the Application started");

        } catch (IOException e) {
            System.err.println("❌ Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility method to read the file content as a String
    private static String readJsonFile(org.springframework.core.io.Resource resource) throws IOException {
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));
    }

    private static void loadPortfolio(String jsonContent, StockService stockService,
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

    public static void processAndSavePortfolio(
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
            clonedInvestment.setStock(originalInvestment.getStock());
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

    private void exportPortfolioToJson(PortfolioService portfolioService, String portfolioName) {
        Portfolio portfolio = portfolioService.getPortfolioByName(portfolioName);
        System.out.println("Found portfolio: " + portfolio);
        portfolioService.exportPortfolioToJson(portfolio.getId());

    }
}
