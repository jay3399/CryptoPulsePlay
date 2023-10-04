package com.example.cryptopulseplay.domian.pricerecord.repository;

import com.example.cryptopulseplay.domian.pricerecord.model.PriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRecordRepository extends JpaRepository<PriceRecord, Long> {

    PriceRecord findTopByOrderByTimeStampDesc();

}
