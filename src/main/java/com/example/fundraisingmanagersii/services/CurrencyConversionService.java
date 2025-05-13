package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.exceptions.ConversionException;
import com.example.fundraisingmanagersii.models.Currency;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

public class CurrencyConversionService {
    public static BigDecimal convert(Currency from, Currency to, BigDecimal amount) {
        String path = "https://open.er-api.com/v6/latest/" + from.toString();

        try{
            InputStream stream = new URL(path).openConnection().getInputStream();
            String json = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining());
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            JsonObject rates = root.getAsJsonObject("rates");
            double rate = rates.get(to.toString()).getAsDouble();
            BigDecimal convertedAmount = amount.multiply(BigDecimal.valueOf(rate));
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(0);
//            System.out.println("Plain Big Decimal: " + convertedAmount.toPlainString());
//            System.out.println("Scaled Big Decimal: " + convertedAmount.setScale(2, RoundingMode.DOWN));
//            System.out.println("Formatted Big Decimal: " + df.format(convertedAmount));
            return convertedAmount.setScale(2, RoundingMode.DOWN);
        } catch (IOException e){
            throw new ConversionException(e.getMessage());
        }
    }
}
