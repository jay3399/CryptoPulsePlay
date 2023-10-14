package com.example.cryptopulseplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CryptoPulsePlayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoPulsePlayApplication.class, args);
    }

}

/**
 * 매시간 게임 결과를 계산해 결과를 산출해 리워드를 지급하는작업 -> 비동기
 * 배팅결과 ,  게임결과조회 -> 데이터베이스 인덱싱처리
 *
 * 중간에 redis 에 데이터를 쌓고 -> 그것을 계산해서 한번에처리
 *
 * 장점:
 * 디비에접근 최소화 , 서버부하-- , 빠른 읽기 및 쓰기
 * 스케일링용이 , 필요에따라 수평확장가능  , 대량 데이터처리하기 적합 , NoSQL 은 RDB 에비해 스케일링에 더 적합하다
 * 여러 인스턴스에 데이터 분산저장 , 병목처리 줄일수잇다.
 *
 *
 * 단점:
 * 데이터일관성 , RDB 와 싱크 관리문제
 * 데이터 유실위험
 * -> 고가용성 이용 .
 *
 *
 * 만약 ,많은 이용자가 몰린다 생각하면 ? Spring Batch
 *
 * 많은수의 배팅결과를 처리하고 , 리워드를 처리 , 업데이트해야하는 상황이 생긴다.
 * 배치 처리를 이용한다 ,대용량 처리에 용이
 *
 * 대량의 데이터를 동기화 해야하는 상황이면 ?
 * 위와 동일
 *
 * 오래된 데이터 , 사용하지않는 데이터를 정리할때
 * +게임결과를 알리는작업.
 *
 * + 병렬처리기능 , 장애발생시 체크포인트 설정후 그곳부터 다시 재시작가능.
 *
 *
 *
 * 요약 -
 *
 * 사용자 게임정보는 , Redis 에 우선 저장
 * 게임종료 -> 배치처리를 이용해 결과 계산및 리워드를 만들어 지급한후 , RDB 에 처리.
 * + 게임종료후 , 실제 가격변동 결과와 , 사용자 투표 결과를 각각 PriceRecord , VotingResult 에 저장
 * 사용자에게 , 게임결과와 그에따른 Reword 처리결과를 알려줌.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */