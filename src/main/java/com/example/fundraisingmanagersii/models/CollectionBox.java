package com.example.fundraisingmanagersii.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "box_money")
    @MapKeyColumn(name = "currency")
    @Column(name = "amount")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Currency, BigDecimal> moneyInside;


    public CollectionBox() {
        moneyInside = new HashMap<>();
        for (Currency currency : Currency.values()) {
            moneyInside.put(currency, BigDecimal.ZERO);
        }
    }

    public Boolean getIsEmpty() {
        return moneyInside.values().stream().filter(a -> a.compareTo(BigDecimal.ZERO) != 0).toList().isEmpty();
    }
}
