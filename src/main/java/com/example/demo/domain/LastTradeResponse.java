package com.example.demo.domain;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class LastTradeResponse {
    private String symbol;
    private Double close;

}
