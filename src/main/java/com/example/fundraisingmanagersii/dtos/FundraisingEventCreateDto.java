package com.example.fundraisingmanagersii.dtos;

import com.example.fundraisingmanagersii.models.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundraisingEventCreateDto {
    private String name;
    private Currency currency;
}
