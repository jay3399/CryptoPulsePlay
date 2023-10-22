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


    public void createPriceRecord(Double startPrice) {

        PriceRecord priceRecord = PriceRecord.create(startPrice);

        redisUtil.setPriceRecord(priceRecord);

    }

    @Transactional
    public Direction updatePriceRecord(Double endPrice) {

        PriceRecord priceRecord = redisUtil.getPriceRecord();

        priceRecord.calculateDirection(endPrice);

        priceRecordRepository.save(priceRecord);

        return priceRecord.getDirection();

    }


}
