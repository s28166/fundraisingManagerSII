package com.example.fundraisingmanagersii.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class FundraisingEvent {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) long id;

    private String name;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
