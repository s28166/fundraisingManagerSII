package com.example.fundraisingmanagersii.repositories;

import com.example.fundraisingmanagersii.models.CollectionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionBoxRepository extends JpaRepository<CollectionBox, Long> {
}
