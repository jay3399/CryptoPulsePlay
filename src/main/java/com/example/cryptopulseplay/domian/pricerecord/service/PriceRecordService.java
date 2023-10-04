package com.example.cryptopulseplay.domian.pricerecord.service;

import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import com.example.cryptopulseplay.domian.pricerecord.repository.PriceRecordRepository;
import com.example.cryptopulseplay.domian.shared.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PriceRecordService {

    private final PriceRecordRepository priceRecordRepository;

    private final RedisUtil redisUtil;


    // 레디스에 저장하고 , 디비에도 저장을할지

    // 아니면 레디스에 저장만하고 , 디비에는 마지막 업데이트때 저장할지. ? (현재)
    @Transactional
    public void createPriceRecord(Double startPrice) {

        PriceRecord priceRecord = PriceRecord.create(startPrice);

        redisUtil.setPriceRecord(priceRecord);

    }

    @Transactional
    public PriceRecord updatePriceRecord(Double endPrice) {

        PriceRecord priceRecord = redisUtil.getPriceRecord();

        priceRecord.calculateDirection(endPrice);

        priceRecordRepository.save(priceRecord);

        return priceRecord;
    }





}
