package com.example.demo.domain;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class LastTrade {
    private double price;
    private long timestamp;
    private int size;

    // Getters and Setters
}