package com.example.demo.application.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.domain.AlphaVantageResponse;
import com.example.demo.domain.StockPriceDTO;
import com.example.demo.domain.AlphaVantageData;
import com.example.demo.domain.LastTradeResponse;
import java.util.Iterator;
import java.util.Map;

@Service
public class StockPriceService {

    private final RestTemplate restTemplate;

    public StockPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String API_URL = "https://www.alphavantage.co/query";
    private final String API_KEY = "35ZFWNKSGQGPT73S"; // 🔑 Replace with your API Key

    public java.util.List<com.example.demo.domain.Dividend> getDividendData(String symbol, String startDate, String endDate) {
//        String url = API_URL + "?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + symbol + "&apikey=" + API_KEY;

         String BASE_URL = "https://api.polygon.io/v3/reference/dividends";


        //String url = "https://api.polygon.io/v3/reference/dividends?ticker=AAPL&ex_dividend_date.gte=2024-01-01&ex_dividend_date.lte=2024-03-01&apiKey=gtJDsKY2TMiYhNiPhEzPgc5ftFZJqRzq"



        //public List<Dividend> getDividends(String ticker) {
          //  LocalDate endDate = LocalDate.now();
            //LocalDate startDate = endDate.minusYears(1);

            String url = org.springframework.web.util.UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("ticker", symbol)
                    .queryParam("ex_dividend_date.gte", startDate)
                    .queryParam("ex_dividend_date.lte", endDate)
                    .queryParam("apiKey", "gtJDsKY2TMiYhNiPhEzPgc5ftFZJqRzq")
                    .toUriString();

        System.out.println("URL: " + url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            java.util.List<Map<String, Object>> results = (java.util.List<Map<String, Object>>) response.get("results");

            return results.stream()
                    .map(result -> new com.example.demo.domain.Dividend(
                            (String) result.get("ex_dividend_date"),
                            result.get("cash_amount").toString()
                    ))
                    .collect(java.util.stream.Collectors.toList());
        }

    public Double getLatestPrice(String ticker, String date) {
        try {

            String url = "https://api.polygon.io/v1/open-close/" + ticker + "/"+ date + "?adjusted=true&apiKey=gtJDsKY2TMiYhNiPhEzPgc5ftFZJqRzq";
        System.out.println(url);
        LastTradeResponse response = restTemplate.getForObject(url, LastTradeResponse.class);
        return response.getClose();

        } catch (Exception e) {
            System.err.println("❌ Error fetching stock price for " + ticker + ": " + e.getMessage());
        }
        return null;
    }

    public StockPriceDTO getLatestStockPrice(String symbol) {
        try {
            String url = API_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + API_KEY;
            AlphaVantageResponse response = restTemplate.getForObject(url, AlphaVantageResponse.class);

            if (response != null) {
                AlphaVantageData latestData = response.getLatestStockData();
                if (latestData != null) {
                    // Return the stock price data as a DTO
                    return new StockPriceDTO(latestData.getClose(), latestData.getTimestamp());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error fetching stock price for " + symbol + ": " + e.getMessage());
        }
        return null;  // Return null in case of an error
    }

}
