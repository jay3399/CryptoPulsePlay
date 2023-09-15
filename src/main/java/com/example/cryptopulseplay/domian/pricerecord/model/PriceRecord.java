package com.example.cryptopulseplay.domian.pricerecord.model;

import com.example.cryptopulseplay.domian.shared.enums.Direction;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class PriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date timeStamp;

    private Double price;

    @Enumerated(EnumType.STRING)
    private Direction direction;




}
