package com.example.cryptopulseplay.application.service;

import com.example.cryptopulseplay.domian.btcprice.service.BtcPriceService;
import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import com.example.cryptopulseplay.domian.pricerecord.service.PriceRecordService;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * 클래스 분리 다시 생각해봐야함 . 응용서비스계층 -> 응용서비스계층(!)   &  도메인서비스계층 이용중.
 */
@Service
@RequiredArgsConstructor
public class GameSchedulingService {


    private final GameAppService gameAppService;
    private final RewordAppService rewordAppService;

    private final BtcPriceService btcPriceService;
    private final PriceRecordService priceRecordService;

    // 매시 정각마다 , 시작가격 기록 .

    /**
     * 흐름 : VotingResult x 0. 매 시간동안 게임에 참여 , 비트코인의 1시간동안 가격이 하락일지 상승일지 포인트를 가지고 게임에 참여 , 각 게임은
     * redis 에 저장.(createGame) 1. 매시 정각 , 스타트가격을 포함해서 PriceRecord 객체를 생성한후 , Redis 에 저장  (
     * priceRecordService.createPriceRecord(startPrice) ) 2. 다음시간으로 넘어가기 직전 59분 59초에 , 마지막 가격을 가져와서
     * Redis 저장값을 가져온뒤 PriceRecord 를 완성후 RDB 에 저장후 상승or하락 값 반환, Redis getAndDelete 로 값 삭제 (Direction
     * direction = priceRecordService.updatePriceRecord(endPrice); ) -> 위 로직을 실행후 반환받은 Direction 을
     * 가지고 이전에 redis 에저장된 game 을 꺼내온후 , direction 과 game 에 저장된 direction 을 비교후 game 결과를 업데이트하고 game
     * 값을 가지고 reword 객체를 생성. 후 리워드 와 게임을 rdb 에 저장. (  gameAppService.calculateGameResult(direction);
     * ) 3. 매시 1분마다 , 위에서 생성한 Pending 상태의 Reword 를 가지고 , 포인트 지급 로직을 실행 (
     * rewordAppService.payReword(); )
     * <p>
     * <p>
     * priceRecord 에 해당회차의 모든 게임들에서
     */

//    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(fixedRate = 20000)
    public void recordStartPrice() {

        double startPrice = btcPriceService.getCurrentPrice().block().getPrice();
        priceRecordService.createPriceRecord(startPrice);
    }

    //매시 59분 59초 , 마지막 가격기록후 게임결과 & 리워드 생성
//    @Scheduled(cron = "59 59 * * * *")
    @Scheduled(fixedRate = 40000)
    public void recordEndPriceAndReword() {
        double endPrice = btcPriceService.getCurrentPrice().block().getPrice();

        Direction direction = priceRecordService.updatePriceRecord(endPrice);

        gameAppService.calculateGameResult(direction);

    }

    @Scheduled(fixedRate = 40000)
    public void recordEndPriceAndRewordV2() {
        double endPrice = btcPriceService.getCurrentPrice().block().getPrice();

        PriceRecord priceRecord = priceRecordService.updatePriceRecordV2(endPrice);

        gameAppService.calculateGameResultV2(priceRecord);

    }

    // 매시 1분 리워드를 가지고 ,유저 포인트 업데이트 및 알림 전송
//    @Scheduled
    @Scheduled(fixedRate = 20000)
    public void payReword() {

        rewordAppService.payReword();

    }


}
