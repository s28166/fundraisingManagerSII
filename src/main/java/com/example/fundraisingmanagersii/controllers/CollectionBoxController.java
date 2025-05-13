package com.example.fundraisingmanagersii.controllers;

import com.example.fundraisingmanagersii.dtos.CollectionBoxGetDto;
import com.example.fundraisingmanagersii.dtos.MoneyAddRequestDto;
import com.example.fundraisingmanagersii.models.CollectionBox;
import com.example.fundraisingmanagersii.services.CollectionBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("collections")
public class CollectionBoxController {
    private final CollectionBoxService collectionBoxService;

    //2. Register a new collection box
    @PostMapping
    public ResponseEntity<Object> registerNewCollectionBox(){
        CollectionBoxGetDto dto = collectionBoxService.registerNewCollectionBox();
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // 3. List all collection boxes. Include information if the box is assigned (but don’t expose to what
    // fundraising event) and if it is empty or not (but don’t expose the actual value in the box)
    @GetMapping
    public ResponseEntity<List<CollectionBoxGetDto>> getAllCollectionBoxes(){
        return ResponseEntity.ok(collectionBoxService.listAllCollectionBoxes());
    }

    // 4. Unregister (remove) a collection box (e.g. in case it was damaged or stolen)
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> unregisterCollectionBox(@PathVariable("id") Long id){
        Long deletedId =  collectionBoxService.unregisterCollectionBox(id);
        return new ResponseEntity<>(String.format("Successfully deleted box with id %s", deletedId), HttpStatus.NO_CONTENT);
    }

    // 5. Assign the collection box to an existing fundraising event
    @PatchMapping(value = "/{id}/assign_to/{eventId}")
    public ResponseEntity<Object> assignToCollectionBox(@PathVariable Long id, @PathVariable Long eventId){
        CollectionBoxGetDto dto = collectionBoxService.assignToCollectionBox(id, eventId);
        return ResponseEntity.ok(String.format("Successfully assigned box with id %s to event %s", dto.getId(), eventId));
    }

    // 6. Put (add) some money inside the collection box
    @PatchMapping(value = "/{id}/add")
    public ResponseEntity<Object> addMoneyToCollectionBox(@PathVariable Long id, @RequestBody MoneyAddRequestDto moneyAddRequestDto){
        CollectionBoxGetDto updated = collectionBoxService.addMoneyToCollectionBox(id, moneyAddRequestDto);
        return ResponseEntity.ok(String.format("Successfully added money to collection with id %s", updated.getId()));
    }

    // 7. Empty the collection box i.e. “transfer” money from the box to the fundraising event’s account
    @PutMapping(value = "/{id}/transfer")
    public ResponseEntity<Object> transferMoneyFromCollectionBox(@PathVariable Long id){
        Long updatedEventId = collectionBoxService.transferMoneyFromCollectionBox(id);
        return ResponseEntity.ok(String.format("Successfully transferred money from collection with the %s id, to the event with the %s id ", id, updatedEventId));
    }


    @GetMapping(value = "/all")
    public ResponseEntity<List<CollectionBox>> getDebugAllCollectionBoxes(){
        return ResponseEntity.ok(collectionBoxService.debugAllBoxes());
    }

    @DeleteMapping(value = "/{id}/debug")
    public ResponseEntity<Object> clearMap(@PathVariable("id") Long id){
        Long deletedId =  collectionBoxService.clearMap(id);
        return new ResponseEntity<>(String.format("Successfully deleted box with id %s", deletedId), HttpStatus.NO_CONTENT);
    }
}
