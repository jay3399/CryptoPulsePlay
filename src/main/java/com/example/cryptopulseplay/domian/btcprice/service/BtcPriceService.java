package com.example.cryptopulseplay.domian.btcprice.service;


import com.example.cryptopulseplay.domian.btcprice.model.BitcoinPrice;
import com.example.cryptopulseplay.infrastructure.api.PriceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BtcPriceService {

    public final PriceClient priceClient;


    public Mono<BitcoinPrice> getCurrentPrice() {

        return priceClient.getBtcPrice().map(BitcoinPrice::new);

    }


}
