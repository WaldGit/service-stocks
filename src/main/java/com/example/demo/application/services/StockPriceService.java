package com.example.demo.application.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.domain.AlphaVantageResponse;
import com.example.demo.domain.StockPriceDTO;
import com.example.demo.domain.AlphaVantageData;

@Service
public class StockPriceService {

    private final RestTemplate restTemplate;

    public StockPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String API_URL = "https://www.alphavantage.co/query";
    private final String API_KEY = "35ZFWNKSGQGPT73S"; // 🔑 Replace with your API Key


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
