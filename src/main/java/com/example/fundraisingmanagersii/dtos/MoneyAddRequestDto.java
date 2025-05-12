package com.example.fundraisingmanagersii.dtos;

import com.example.fundraisingmanagersii.models.Currency;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MoneyAddRequestDto {
    private Currency currency;
    private BigDecimal amount;
}
