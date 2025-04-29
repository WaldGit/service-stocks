package com.example.demo.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "stocks")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
@lombok.Setter
@lombok.Getter
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String website;
}
