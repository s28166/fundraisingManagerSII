package com.example.fundraisingmanagersii.configs;

import com.example.fundraisingmanagersii.models.CollectionBox;
import com.example.fundraisingmanagersii.models.Currency;
import com.example.fundraisingmanagersii.models.FundraisingEvent;
import com.example.fundraisingmanagersii.repositories.CollectionBoxRepository;
import com.example.fundraisingmanagersii.repositories.FundraisingEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.HashMap;

@Configuration
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    CommandLineRunner loadExampleDatabase(CollectionBoxRepository collectionBoxRepository, FundraisingEventRepository fundraisingEventRepository) {
        return (args) -> {
            logger.info("Loading example database...");
            var databaseEntryEvent = new FundraisingEvent();
            databaseEntryEvent.setName("Caritas");
            databaseEntryEvent.setCurrency(Currency.PLN);
            databaseEntryEvent.setBalance(BigDecimal.ZERO);
            logger.info("Preloading example fundraising event{}", fundraisingEventRepository.save(databaseEntryEvent));
            var databaseEntryBox = new CollectionBox();
//            databaseEntryBox.setMoneyInside(new HashMap<>());
//            databaseEntryBox.setIsEmpty(true);
            databaseEntryBox.setFundraisingEvent(databaseEntryEvent);
            logger.info("Preloading example collection box {}", collectionBoxRepository.save(databaseEntryBox));
            databaseEntryBox = new CollectionBox();
//            databaseEntryBox.setMoneyInside(new HashMap<>());
//            databaseEntryBox.setIsEmpty(true);
            databaseEntryBox.setFundraisingEvent(null);
            logger.info("Preloading example collection box {}", collectionBoxRepository.save(databaseEntryBox));
        };
    }
}