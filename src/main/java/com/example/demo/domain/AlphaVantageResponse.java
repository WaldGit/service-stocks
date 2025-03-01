package com.example.demo.domain;
import com.example.demo.domain.StockPriceDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageResponse {

    @JsonProperty("Global Quote")
    private AlphaVantageData globalQuote;

    public AlphaVantageData getLatestStockData() {
        return globalQuote;
    }

    public void setGlobalQuote(AlphaVantageData globalQuote) {
        this.globalQuote = globalQuote;
    }
}


