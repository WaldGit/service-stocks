package com.example.demo.application.services;

import com.example.demo.application.ports.out.PortfolioRepository;
import com.example.demo.domain.InvestmentMetricsDTO;
import com.example.demo.domain.Portfolio;
import com.example.demo.domain.StockInvestment;
import com.example.demo.domain.StockTranche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator;
import java.io.File;
import java.io.IOException;


import com.example.demo.adapters.out.persistence.StockInvestmentRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@lombok.RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockInvestmentRepository stockInvestmentRepository;
    private final StockPriceService stockPriceService;
    private final ObjectMapper objectMapper; // ✅ JSON Mapper


    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    public Portfolio getPortfolioById(UUID id) {
        return portfolioRepository.findById(id).orElse(null);
    }

    public void deletePortfolio(UUID id) {
        portfolioRepository.deleteById(id);
    }

    public void processAndSavePortfolio(Portfolio portfolio) {
        // Define the amount to invest in each tranche (500 EUR in this example)
        double totalInvestmentAmount = 500;

        // Loop through each stock investment
        for (com.example.demo.domain.StockInvestment stockInvestment : portfolio.getStockInvestments()) {
            // Loop through each tranche
            for (com.example.demo.domain.StockTranche tranche : stockInvestment.getTranches()) {
                // Calculate the quantity for each tranche
                int quantity = (int) (totalInvestmentAmount / tranche.getPricePerShare());
                tranche.setQuantity(quantity); // Set the calculated quantity
            }
        }

        // Save the portfolio, stock investments, and tranches to the database
        portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolioByName(String name) {
        return portfolioRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with name: " + name));
    }

    public Optional<Portfolio> findById(UUID id) {
        return portfolioRepository.findById(id);
    }

    public Portfolio getPortfolioForExport(UUID portfolioId) {
        return portfolioRepository.findPortfolioWithInvestments(portfolioId);
    }


    public Portfolio getPortfolioWithMetrics(UUID portfolioId) {
        // Fetch the portfolio from the repository
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        double totalPortfolioInvestment = 0.0;

        // First, calculate the total portfolio investment and dividends per investment
        for (StockInvestment investment : portfolio.getStockInvestments()) {
            InvestmentMetricsDTO metrics = calculateInvestmentMetrics(investment);
            investment.setMetrics(metrics);
            totalPortfolioInvestment += metrics.getTotalPrice(); // Sum total invested amount

            // ✅ Calculate total dividends received for this investment (sum of tranche dividends)
            double totalDividends = investment.getTranches().stream()
                    .filter(tranche -> tranche.getDividends() != null) // ✅ Ensure no null values
                    .mapToDouble(StockTranche::getDividends) // ✅ Extract and sum dividends
                    .sum();

            metrics.setTotalDividends(totalDividends); // Store in metrics

            //totalPortfolioDividends += totalDividends; // Sum up for the entire portfolio
        }

        double totalGainLoss = 0.0;

        // Now, calculate the % allocation for each investment
        for (StockInvestment investment : portfolio.getStockInvestments()) {
            InvestmentMetricsDTO metrics = investment.getMetrics();
            if (totalPortfolioInvestment > 0) {
                double allocation = (metrics.getTotalPrice() / totalPortfolioInvestment) * 100;
                metrics.setAllocationPercentage(allocation); // ✅ Store % allocation
            } else {
                metrics.setAllocationPercentage(0.0);
            }
            totalGainLoss += metrics.getTotalGainLoss();
        }

        portfolio.setTotalGainLoss(totalGainLoss); // ✅ Store total gain/loss

        // ✅ Calculate total percentage gain
        double totalPercentageGain = (totalPortfolioInvestment > 0) ?
                (totalGainLoss / totalPortfolioInvestment) * 100 : 0.0;

        portfolio.setTotalPercentageGain(totalPercentageGain); // ✅ Store in portfolio


        return portfolio;
    }



    public InvestmentMetricsDTO calculateInvestmentMetrics(StockInvestment investment) {
        double totalGainLoss = 0.0;
        double totalInvestment = 0.0;
        double totalShares = 0.0;
        double totalPrice = 0.0;

        for (StockTranche tranche : investment.getTranches()) {
            // Calculate gain/loss for the tranche
            double gainLoss = (investment.getCurrentPrice() - tranche.getPricePerShare()) * tranche.getQuantity();
            totalGainLoss += gainLoss;

            // Total investment amount
            totalInvestment += tranche.getPricePerShare() * tranche.getQuantity();

            // Total number of shares
            totalShares += tranche.getQuantity();

            // Total price paid for all shares
            totalPrice += tranche.getPricePerShare() * tranche.getQuantity();

            // Calculate percentage gain for the tranche
            double percentageGain = ((investment.getCurrentPrice() - tranche.getPricePerShare()) / tranche.getPricePerShare()) * 100;
            tranche.setPercentageGain(percentageGain);
        }

        // Avoid division by zero
        double returnPercentage = (totalInvestment == 0) ? 0.0 : (totalGainLoss / totalInvestment) * 100;
        double averagePricePerShare = (totalShares == 0) ? 0.0 : totalPrice / totalShares;

        // Return the updated metrics
        return new InvestmentMetricsDTO(returnPercentage, totalShares, averagePricePerShare, totalPrice, totalGainLoss,0,0);
    }

    //update each investement check the tranches en check the date and the date now and get the dividends from the number of shares
    //

    @Transactional
    public void updatePortfolioMetricsDividends(UUID portfolioId) {
            Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));


        // ✅ Sort investments by `currentDatePrice` (oldest first) & get oldest 25
        List<StockInvestment> sortedInvestments = portfolio.getStockInvestments().stream()
                .filter(investment -> !investment.isClosed())
                .sorted(Comparator.comparing(StockInvestment::getCurrentDatePrice, Comparator.nullsLast(LocalDate::compareTo)))
                .limit(25)
                .toList();

        // ✅ Fetch latest stock prices and update database
        for (StockInvestment investment : sortedInvestments) {
            investment.getTranches().forEach(tranche -> {

            });
        }
    }

    public static LocalDate previousBusinessDay(LocalDate date) {
        LocalDate prev = date.minusDays(1);
        DayOfWeek dow = prev.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY) {
            // If it lands on Saturday, go back 1 more day → Friday
            prev = prev.minusDays(1);
        } else if (dow == DayOfWeek.SUNDAY) {
            // If it lands on Sunday, go back 2 more days → Friday
            prev = prev.minusDays(2);
        }
        return prev;
    }

    @Transactional
    public Portfolio updatePortfolioMetrics(UUID portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        double totalPortfolioInvestment = 0.0;
        LocalDate today = LocalDate.now();

        LocalDate yesterday = this.previousBusinessDay(today);
        // Define the desired format
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the date
        String formattedYesterday = yesterday.format(formatter);
        System.out.println("formattedYesterday "+ formattedYesterday);



        // ✅ Sort investments by `currentDatePrice` (oldest first) & get oldest 25
        List<StockInvestment> sortedInvestments = portfolio.getStockInvestments().stream()
                .filter(investment -> !investment.isClosed())
                .sorted(Comparator.comparing(StockInvestment::getCurrentDatePrice, Comparator.nullsLast(LocalDate::compareTo)))
                .toList();

        int index = 0;
        // ✅ Fetch latest stock prices and update database
        for (StockInvestment investment : sortedInvestments) {

            System.out.println("Investment " + index + " " + sortedInvestments.size());

             Double d =stockPriceService.getLatestPrice(investment.getStock().getName(), formattedYesterday);
             System.out.println(investment.getStock().getName() + " " + d);

            try {
                Thread.sleep(15000); // 10,000 milliseconds = 10 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // good practice
                System.out.println("Sleep interrupted");
            }
            //StockPriceDTO stockPrice = stockPriceService.getLatestStockPrice(investment.getTicker());

                if(d != null)  {
                    System.out.println("THERE IS A DATE :");
                    investment.setCurrentPrice(d);
                    investment.setCurrentDatePrice(yesterday);

                }

                index += 1;

        }

        // ✅ Recalculate investment metrics & update allocations
        double totalGainLoss = 0.0;
        for (StockInvestment investment : portfolio.getStockInvestments()) {
            InvestmentMetricsDTO metrics = calculateInvestmentMetrics(investment);
            investment.setMetrics(metrics);
            totalPortfolioInvestment += metrics.getTotalPrice();
            totalGainLoss += metrics.getTotalGainLoss();
        }

        for (StockInvestment investment : portfolio.getStockInvestments()) {
            InvestmentMetricsDTO metrics = investment.getMetrics();
            if (totalPortfolioInvestment > 0) {
                metrics.setAllocationPercentage((metrics.getTotalPrice() / totalPortfolioInvestment) * 100);
            } else {
                metrics.setAllocationPercentage(0.0);
            }
        }

        // ✅ Save updated investments back to the database
        stockInvestmentRepository.saveAll(portfolio.getStockInvestments());

        // ✅ Update portfolio total gain/loss
        portfolio.setTotalGainLoss(totalGainLoss);
        portfolioRepository.save(portfolio); // Save updated portfolio

        return portfolio;
    }

    public void exportPortfolioToJson(UUID portfolioId) {
        Portfolio portfolioOpt = this.getPortfolioForExport(portfolioId);


            try {
                // ✅ Define output file path
                String filePath = "exported_portfolio_" + portfolioOpt.getName() +".json";
                File file = new File(filePath);

                // ✅ Convert DTO to JSON and save it
                objectMapper.writeValue(file, portfolioOpt);

                System.out.println("✅ Portfolio exported to JSON: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("❌ Error exporting portfolio: " + e.getMessage());
            }

    }


}

