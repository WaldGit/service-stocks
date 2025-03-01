package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockData {

    @JsonProperty("1. open")
    private Double open;

    @JsonProperty("2. high")
    private Double high;

    @JsonProperty("3. low")
    private Double low;

    @JsonProperty("4. close")
    private Double close;

    @JsonProperty("5. volume")
    private Long volume;

    public Double getOpen() {
        return open;
    }

    public Double getHigh() {
        return high;
    }

    public Double getLow() {
        return low;
    }

    public Double getClose() {
        return close;
    }

    public Long getVolume() {
        return volume;
    }
}
