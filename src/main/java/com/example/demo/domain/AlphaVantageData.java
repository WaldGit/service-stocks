package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AlphaVantageData {

    @JsonProperty("05. price")
    private Double close;

    @JsonProperty("07. latest trading day")
    private String latestTradingDay; // String format (YYYY-MM-DD)

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public LocalDate getTimestamp() {
        return LocalDate.parse(latestTradingDay, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setLatestTradingDay(String latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }
}
