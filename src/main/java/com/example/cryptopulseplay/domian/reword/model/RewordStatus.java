package com.example.cryptopulseplay.domian.reword.model;

public enum RewordStatus {

    PAID , REJECTED , PENDING


}

/**
 * 리워드를 즉시지급 해야할까 , 아니면 지연을 줄까
 *
 *
 * 사용자관점 : 바로 반영되는것이 당연히 좋다.
 *
 * 관리자괸점 :
 * 1.게임이 끝나고 ,바로 지급시 한번에 많은사용자가 몰릴수있어 서버에 부하를 줄수있다.
 * 2.만약 결과에 문제가 생길시 ,바로 지급시 문제가 생길수있다.
 */