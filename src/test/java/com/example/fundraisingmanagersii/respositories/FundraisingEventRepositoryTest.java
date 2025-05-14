package com.example.fundraisingmanagersii.respositories;

import com.example.fundraisingmanagersii.models.Currency;
import com.example.fundraisingmanagersii.models.FundraisingEvent;
import com.example.fundraisingmanagersii.repositories.FundraisingEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FundraisingEventRepositoryTest {
    @Autowired
    private FundraisingEventRepository fundraisingEventRepository;

    @Test
    public void shouldSaveAndFindFundraisingEvent() {
        FundraisingEvent event = new FundraisingEvent();
        event.setBalance(BigDecimal.ZERO);
        event.setCurrency(Currency.EUR);
        event.setName("Test event");

        FundraisingEvent savedEvent = fundraisingEventRepository.save(event);
        Optional<FundraisingEvent> found = fundraisingEventRepository.findById(savedEvent.getId());

        assertTrue(found.isPresent());
        assertEquals(savedEvent.getId(), found.get().getId());
        assertEquals(savedEvent.getBalance(), found.get().getBalance());
        assertEquals(savedEvent.getCurrency(), found.get().getCurrency());
        assertEquals(savedEvent.getName(), found.get().getName());
    }

    @Test
    public void shouldFindAllFundraisingEvent() {
        FundraisingEvent event = new FundraisingEvent();
        FundraisingEvent event2 = new FundraisingEvent();
        event.setBalance(BigDecimal.ZERO);
        event2.setBalance(BigDecimal.ZERO);
        event.setCurrency(Currency.EUR);
        event2.setCurrency(Currency.EUR);
        event.setName("Test event");
        event2.setName("Test event2");

        FundraisingEvent savedEvent = fundraisingEventRepository.save(event);
        FundraisingEvent savedEvent2 = fundraisingEventRepository.save(event2);

        List<FundraisingEvent> events = fundraisingEventRepository.findAll();

        assertTrue(events.contains(savedEvent));
        assertTrue(events.contains(savedEvent2));
        assertEquals(2, events.size());
        assertEquals(savedEvent.getId(), events.getFirst().getId());
        assertEquals(savedEvent.getBalance(), events.getFirst().getBalance());
        assertEquals(savedEvent.getCurrency(), events.getFirst().getCurrency());
        assertEquals(savedEvent.getName(), events.getFirst().getName());
        assertEquals(savedEvent2.getBalance(), events.get(1).getBalance());
        assertEquals(savedEvent2.getCurrency(), events.get(1).getCurrency());
        assertEquals(savedEvent2.getName(), events.get(1).getName());
    }
}
