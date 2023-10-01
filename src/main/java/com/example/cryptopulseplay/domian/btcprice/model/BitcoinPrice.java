package com.example.cryptopulseplay.domian.btcprice.model;

import lombok.Getter;

@Getter
public class BitcoinPrice {

    private final double price;

    public BitcoinPrice(double price) {
        this.price = price;
    }


}

