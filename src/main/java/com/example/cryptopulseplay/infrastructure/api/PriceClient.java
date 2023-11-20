package com.example.cryptopulseplay.infrastructure.api;


import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@Slf4j
public class PriceClient {

    private final WebClient webClient;

    public PriceClient() {
        this.webClient = WebClient.create("https://api.coingecko.com");
    }


    public Mono<Double> getBtcPrice() {
        return
        webClient.get()
                .uri("/api/v3/simple/price?ids=bitcoin&vs_currencies=usd")
                .retrieve()
                .bodyToMono(BtcPriceResponse.class)
                .map(response -> response.bitcoin.usd)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10)))
                .doOnSuccess( p -> log.info("success"))
                .doOnError(e -> log.error("error", e));
    }

    private static class BtcPriceResponse{
        public BitcoinPrice bitcoin;

    }
    private static class BitcoinPrice{
        public double usd;

    }

}
