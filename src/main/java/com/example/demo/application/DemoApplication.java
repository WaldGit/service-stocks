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
import com.example.demo.application.services.StockPriceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")

public class DemoApplication {

    private static final String TARGET_PORTFOLIO_NAME = "High Yield Portfolio"; // 🔄 Change this to the desired name
    //private static final String TARGET_PORTFOLIO_NAME = "Dividend Growth Stock Portfolio"; // 🔄 Change this to the desired name

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner run(StockService stockService, StockTrancheService stockTrancheService, PortfolioService portfolioService, com.fasterxml.jackson.databind.ObjectMapper objectMapper, StockPriceService stockPriceService ) {
        return args -> {

            System.out.println(TARGET_PORTFOLIO_NAME);

           //ExportIn.importFetchFromFiles(portfolioService);
           //ExportIn.exportPortfolioToJson(portfolioService,TARGET_PORTFOLIO_NAME);

           //this.updatePortfolioMetrics(portfolioService,stockPriceService);
            //this.getDividends(stockPriceService,portfolioService,stockTrancheService);
        };
    }

    private void getDividends(StockPriceService stockPriceService, PortfolioService portfolioService,StockTrancheService stockTrancheService){
        System.out.println("get Dividends...");


        Portfolio portfolio = portfolioService.getPortfolioByName(TARGET_PORTFOLIO_NAME);
        portfolio.getStockInvestments().forEach((s) -> {

            stockTrancheService.getAllTranchesByStockId(s.getId()).forEach((t) -> {

                if (Boolean.TRUE.equals(t.getDividendDone())) {
                    return;
                }

                System.out.println("processing " + s.getTicker() + " - " + t.getPurchaseDate());

                   try {
                        Thread.sleep(15000); // 10,000 milliseconds = 10 seconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // good practice
                        System.out.println("Sleep interrupted");
                    }

                   System.out.println("Process dividends...");

                    List<com.example.demo.domain.Dividend> ds;
                    if(s.isClosed()  == false){
                        System.out.println("isClosed: " + s.isClosed() + "t.getPurchaseDate " + t.getPurchaseDate() + "-" + java.time.LocalDate.now());
                        ds = stockPriceService.getDividendData(s.getTicker(), t.getPurchaseDate().format(java.time.format.DateTimeFormatter.ISO_DATE),
                                java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_DATE));// Convert purchase date to string
                    } else {
                        System.out.println("isClosed: " + s.isClosed() + "t.getPurchaseDate " + t.getPurchaseDate() + "-" +s.getClosedDate());

                        ds = stockPriceService.getDividendData(s.getTicker(), t.getPurchaseDate().format(java.time.format.DateTimeFormatter.ISO_DATE),s.getClosedDate().format(java.time.format.DateTimeFormatter.ISO_DATE));
                    }

                        AtomicReference<Double> total = new AtomicReference<>((double) 0);
                ds.forEach((d) -> {
                    System.out.println(d.getDate() + "-" + d.getAmount());
                    total.updateAndGet(v -> new Double((double) (v + Double.parseDouble(d.getAmount()))));
                });

                t.setDividends(total.get()*t.getQuantity());
                t.setDividendDone(true);
                    System.out.println("total: " + t.getDividends() + " for " + t.getQuantity() + " shares");

                stockTrancheService.save(t);

            });



        });



        }

    private void updatePortfolioMetrics(PortfolioService portfolioService,StockPriceService  stockPriceService) {
        Portfolio portfolio = portfolioService.getPortfolioByName(TARGET_PORTFOLIO_NAME);
        System.out.println("Found portfolio: " + portfolio);
        portfolioService.updatePortfolioMetrics(portfolio.getId());
        System.out.println("Done updating portfolio metrics..");

        //stockPriceService.getLatestPrice("KM", "2025-04-25");
    }














}

