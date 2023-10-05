package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.btcprice.service.BtcPriceService;
import com.example.cryptopulseplay.domian.pricerecord.service.PriceRecordService;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * 클래스 분리 다시 생각해봐야함 . 응용서비스계층 -> 응용서비스계층(!) , 도메인서비스계층 이용중.
 */
@Service
@RequiredArgsConstructor
public class GameSchedulingService {


    private final GameAppService gameAppService;

    private final BtcPriceService btcPriceService;
    private final PriceRecordService priceRecordService;


    // 매시 정각마다 , 시작가격 기록 .

    /**
     * 흐름 : 매시 정각 , 스타트가격을 포함해서 PriceRecord 객페 생성후 , Redis 에 저장 시간 넘어가기 직전 , 59분 59초에 redis 객체 꺼내와서
     * , 마지막 가격 가져와서 스타트가격 , 엔드가격 , 방향을 포함한 객체 완성후 저장
     * <p>
     * 이후 , 정각에 이전에 저장한 객체의 결과를 꺼내와서 , 그것을 토대로 게임결과 산출.
     * <p>
야한다*
     *
     *
     */
    @Scheduled(cron = "0 0 * * * *")
    public void recordStartPrice() {
        double startPrice = btcPriceService.getCurrentPrice().block().getPrice();
        priceRecordService.createPriceRecord(startPrice);
    }

    //매시 59분 59초 , 마지막 가격기록후 게임결과 & 리워드 생성
    @Scheduled(cron = "59 59 * * * *")
    public void recordEndPriceAndReword() {
        double endPrice = btcPriceService.getCurrentPrice().block().getPrice();
        Direction direction = priceRecordService.updatePriceRecord(endPrice);
        gameAppService.calculateGameResult(direction);
    }

    // 리워드를 가지고 ,  유저 포인트 업데이트 및 포인트 업데이트
    public void payReword() {




    }







}
