package com.example.cryptopulseplay.domian.pricerecord.model;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class PriceRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timeStamp;

    private Double startPrice;
    private Double endPrice;
    @Enumerated(EnumType.STRING)
    private Direction direction = Direction.PENDING;

    public void set(Double startPrice) {
        this.startPrice = startPrice;
        this.timeStamp = LocalDateTime.now();
    }

    public void calculateDirection(Double endPrice) {
        this.direction = endPrice > startPrice ? Direction.UP : Direction.DOWN;
    }

    public static PriceRecord create(Double startPrice) {

        PriceRecord priceRecord = new PriceRecord();

        priceRecord.set(startPrice);

        return priceRecord;
    }




}
