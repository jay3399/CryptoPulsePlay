package com.example.cryptopulseplay.domian.pricerecord.service;

import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import com.example.cryptopulseplay.domian.pricerecord.repository.PriceRecordRepository;
import com.example.cryptopulseplay.domian.shared.enums.Direction;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PriceRecordService {

    private final PriceRecordRepository priceRecordRepository;

    private final RedisUtil redisUtil;


    /**
     * 게임 시작가격을 받고 , PriceRecord 를 생성후 Redis 에 저장.
     * @param startPrice 시작가격.
     */
    public void createPriceRecord(Double startPrice) {

        PriceRecord priceRecord = PriceRecord.create(startPrice);

        redisUtil.setPriceRecord(priceRecord);

    }

    /**
     * 게임 끝나기전 , 마지막 가격을 받아서 게임의 결과 방향을 계산후 DB 에 저장.
     * @param endPrice 마지막가격.
     * @return 해당게임의 최종 결괴.
     */
    @Transactional
    public Direction updatePriceRecord(Double endPrice) {

        PriceRecord priceRecord = redisUtil.getPriceRecord();

        priceRecord.calculateDirection(endPrice);

        priceRecordRepository.save(priceRecord);

        return priceRecord.getDirection();

    }

    @Transactional
    public PriceRecord updatePriceRecordV2(Double endPrice) {

        PriceRecord priceRecord = redisUtil.getPriceRecord();

        priceRecord.calculateDirection(endPrice);

        PriceRecord priceRecord1 = priceRecordRepository.save(priceRecord);

        return priceRecord1;

    }



}
