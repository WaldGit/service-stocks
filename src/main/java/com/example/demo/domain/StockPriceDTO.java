package com.example.demo.domain;

import java.time.LocalDate;

public class StockPriceDTO {
    private Double price;
    private LocalDate date;

    public StockPriceDTO(Double price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return date;
    }
}
