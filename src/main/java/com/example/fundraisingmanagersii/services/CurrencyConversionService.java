package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.exceptions.ConversionException;
import com.example.fundraisingmanagersii.models.Currency;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {
    private final WebClient webClient;

    public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {
        try{
            String path = webClient.get().uri(from.toString()).retrieve().bodyToMono(String.class).block();
            JsonObject root = JsonParser.parseString(path).getAsJsonObject();
            JsonObject rates = root.getAsJsonObject("rates");

            double rate = rates.get(to.toString()).getAsDouble();
            return amount.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.DOWN);
        } catch (Exception e){
            throw new ConversionException(e.getMessage());
        }
    }
}
