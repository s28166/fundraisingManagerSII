package com.example.fundraisingmanagersii.repositories;

import com.example.fundraisingmanagersii.models.FundraisingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraisingEventRepository extends JpaRepository<FundraisingEvent, Long> {
}
