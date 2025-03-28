package com.example.demo.domain;

public class Dividend {
    private String date;
    private String amount;

    public Dividend(String date, String amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}
