package com.example.fundraisingmanagersii.dtos;

import com.example.fundraisingmanagersii.models.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FundraisingEventGetDto {
    private String name;
    private BigDecimal balance;
    private Currency currency;
}
