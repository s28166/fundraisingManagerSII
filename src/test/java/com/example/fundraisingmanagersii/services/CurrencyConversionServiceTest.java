package com.example.fundraisingmanagersii.services;

import com.example.fundraisingmanagersii.models.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CurrencyConversionServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    @Test
    public void convert_shouldConvertCurrency() {
        Currency from = Currency.USD;
        Currency to = Currency.EUR;
        BigDecimal amount = new BigDecimal("100.00");

        String fakeJson = "{ \"rates\": { \"EUR\": 0.85 } }";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(from.toString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(fakeJson));

        BigDecimal result = currencyConversionService.convert(from, to, amount);

        assertEquals(new BigDecimal("85.00"), result);

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("USD");
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(String.class);
    }
}
