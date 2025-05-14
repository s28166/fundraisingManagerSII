package com.example.fundraisingmanagersii.respositories;

import com.example.fundraisingmanagersii.models.CollectionBox;
import com.example.fundraisingmanagersii.repositories.CollectionBoxRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CollectionBoxRepositoryTest {
    @Autowired
    private CollectionBoxRepository collectionBoxRepository;

    @Test
    public void shouldSaveAndFindCollectionBox(){
        CollectionBox box = new CollectionBox();
        box.setFundraisingEvent(null);

        CollectionBox savedBox = collectionBoxRepository.save(box);

        Optional<CollectionBox> foundBox = collectionBoxRepository.findById(savedBox.getId());

        assertTrue(foundBox.isPresent());
        assertEquals(savedBox.getId(), foundBox.get().getId());
        assertEquals(savedBox.getFundraisingEvent(), savedBox.getFundraisingEvent());
        assertEquals(savedBox.getIsEmpty(), savedBox.getIsEmpty());
    }

    @Test
    public void shouldFindAllCollectionBox(){
        CollectionBox box = new CollectionBox();
        CollectionBox box2 = new CollectionBox();
        box.setFundraisingEvent(null);
        box2.setFundraisingEvent(null);

        CollectionBox savedBox = collectionBoxRepository.save(box);
        CollectionBox savedBox2 = collectionBoxRepository.save(box2);

        List<CollectionBox> savedBoxes = collectionBoxRepository.findAll();

        assertTrue(savedBoxes.contains(savedBox));
        assertTrue(savedBoxes.contains(savedBox2));
        assertEquals(2, savedBoxes.size());
        assertEquals(savedBox.getId(), savedBoxes.getFirst().getId());
        assertEquals(savedBox.getFundraisingEvent(), savedBoxes.getFirst().getFundraisingEvent());
        assertEquals(savedBox.getIsEmpty(), savedBoxes.getFirst().getIsEmpty());
        assertEquals(savedBox2.getId(), savedBoxes.getLast().getId());
        assertEquals(savedBox2.getFundraisingEvent(), savedBoxes.getLast().getFundraisingEvent());
        assertEquals(savedBox2.getIsEmpty(), savedBoxes.getLast().getIsEmpty());
    }
}
