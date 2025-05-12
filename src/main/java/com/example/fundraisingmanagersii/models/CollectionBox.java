package com.example.fundraisingmanagersii.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@Data
public class CollectionBox {
    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @ManyToOne
    private FundraisingEvent fundraisingEvent;

    private Boolean isEmpty;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "box_money")
    @MapKeyColumn(name = "currency")
    @Column(name = "amount")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Currency, BigDecimal> moneyInside;

    @PrePersist
    @PreUpdate
    private void initialize() {
        moneyInside = new HashMap<>();
        for (Currency currency : Currency.values()) {
            moneyInside.put(currency, BigDecimal.ZERO);
        }
    }

    public Boolean getIsEmpty() {
        return moneyInside.values().stream().filter(a -> !a.equals(BigDecimal.ZERO)).toList().isEmpty();
    }
}
