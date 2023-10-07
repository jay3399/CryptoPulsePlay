package com.example.cryptopulseplay.domian.reword.service;

import com.example.cryptopulseplay.domian.reword.model.Notification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class NotificationService {

    // Project Reactor 의 Sink API 를 사용하여 여러 데이터항목을 처리하는 sink 를 정의.
    // sink 는 notification 객체를 처리 , 구독자에게 데이터를 전송
    // multicast : sink 가 여러 구독자에게 데이터를 전송할수있도록함
    // onBackpressureBuffer : backpressure 상황에서 버퍼링을 사용 , 데이터를 전송할 준비가 되지않은 구독자가 있을때 데이터를 일시적으로 버퍼에 저장.
    /**
     * 포인트 지급과같은 해당방식을 이용.
     * 동일한 이벤트스트림 구독가능 -> Sinks.Many
     * 이벤트버퍼링가능 : 일시적으로 연결이 끊어졌을경우 , 누락된 이벤트를 받을수있다 (!)
     * 내부이벤트에 반응
     */

    private final Sinks.Many<Notification> notificationSink = Sinks.many().multicast()
            .onBackpressureBuffer();


    public void notify(Notification notification) {
        notificationSink.tryEmitNext(notification);
    }

    // flux 스트림을 변환후 반환.  해당스트림은 sse 를 통해 구독할수있음.

    /**
     * Flux - 0 ~ N 개의 아이템을 비동기적으로 차리
     * 백프레셔지원 - 소비자가 처리할수있는 데이터향을 제어 ,리소스 과부화 방지
     * 함수형 프로그래밍
     */
    public Flux<Notification> getNotification() {
        return notificationSink.asFlux();
    }


}
