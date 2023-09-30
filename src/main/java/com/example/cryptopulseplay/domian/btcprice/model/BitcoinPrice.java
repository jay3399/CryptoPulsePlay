package com.example.cryptopulseplay.domian.btcprice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitcoinPrice {

    private final double price;

    public BitcoinPrice(double price) {
        this.price = price;
    }


}

