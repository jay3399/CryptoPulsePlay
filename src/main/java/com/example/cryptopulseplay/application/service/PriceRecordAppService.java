package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.btcprice.service.BtcPriceService;
import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import com.example.cryptopulseplay.domian.pricerecord.service.PriceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceRecordAppService {

    private final BtcPriceService btcPriceService;

    private final PriceRecordService priceRecordService;


    // 매시 정각마다 , 시작가격 기록 .
    @Scheduled(cron ="0 0 * * * *" )
    public void recordStartPrice() {
        double startPrice = btcPriceService.getCurrentPrice().block().getPrice();
        priceRecordService.createPriceRecord(startPrice);
    }

    //매시 59분 59초 , 마지막 가격기록후 방향 산출

    @Scheduled(cron = "59 59 * * * *")
    public void recordEndPriceAndResult() {
        double endPrice = btcPriceService.getCurrentPrice().block().getPrice();

        PriceRecord priceRecord = priceRecordService.updatePriceRecord(endPrice);


    }


}
