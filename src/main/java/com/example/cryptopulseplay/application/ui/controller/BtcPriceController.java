package com.example.cryptopulseplay.application.ui.controller;


import com.example.cryptopulseplay.domian.btcprice.model.BitcoinPrice;
import com.example.cryptopulseplay.domian.btcprice.service.BtcPriceService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


/**
 * Websocket x
 * SSE 이용
 * - 단방향 통신 , http 프로토콜을 그대로 따름, 웹표준 , 웹인프라와 호환성 ++ , 추가적인 라이브러리 x , 연결이 끊어졌을경우 자동 재연결 시도
 */
@RestController
@RequiredArgsConstructor
public class BtcPriceController {

    private final BtcPriceService btcPriceService;


    /**
     * @return API 이용 30초마다 가격호출.
     */
    @GetMapping(value = "/btc-price" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BitcoinPrice> getBtcPrice() {

        return Flux.interval(Duration.ofSeconds(30)).flatMap(s -> btcPriceService.getCurrentPrice());

    }


}
