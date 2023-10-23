//package com.example.cryptopulseplay.domian.pricerecord.service;
//
//import com.example.cryptopulseplay.domian.pricerecord.model.VoteCountEvent;
//import com.example.cryptopulseplay.domian.shared.enums.Direction;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class PriceRecordEventHandler {
//
//    private final PriceRecordService priceRecordService;
//
//    @EventListener
//    public void handleVoteCounts(VoteCountEvent event) {
//
//        Direction actualDirection = event.getActualDirection();
//
//        priceRecordService.updateCountOnDirection(actualDirection);
//
//
//    }
//
//}
