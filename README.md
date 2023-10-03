# CryptoPulsePlay (예측게임)

## 목차
- [요구사항 분석](#요구사항-분석)
    - [기능 명세](#기능-명세)
    - [사용자 스토리](#사용자-스토리)
- [기술 스택](#기술-스택)
- [데이터베이스 구조](#데이터베이스-구조)

## 요구사항 분석

### 기능 명세

1. **사용자 인증**: 사용자는 이메일 인증 방식으로 로그인해야 한다.
2. **포인트 시스템**: 사용자는 포인트를 가질 수 있으며, 포인트에 따라 등급이 나뉘고, 예측에 이를 사용할 수 있다. (추후에 포인트에 따른 혜택 추가 예정)
3. **예측 게임**: 사용자는 매 시간 특정 시간에 포인트를 이용해서 상승/하락 예측을 할 수 있다.
4. **현황 조회**: 사용자는 예측 현황을 볼 수 있다.
5. **결과 확인**: 매 시간이 끝나면 결과를 확인할 수 있고, 맞힌 경우 포인트의 두 배를 얻고, 아닐 경우 모든 포인트를 잃게 된다.
6. **차트 보기**: 사용자는 각 시간대별로 실제 결과와 사용자 투표의 과반수 결과를 차트로 확인이 가능하다.
7. **참여 이력 조회**: 본인이 참여한 이력을 조회하고, 총 얻은 금액과 예측 성공 확률을 확인할 수 있다.

### 사용자 스토리

1. 사용자는 로그인/로그아웃을 할 수 있어야 한다.
2. 사용자는 현재 비트코인의 가격과, 차트를 볼 수 있어야 한다.
3. 사용자는 포인트를 이용해 상승, 또는 하락에 예측 할 수 있어야 한다.
4. 사용자는 현재 예측 상황을 볼 수 있다.
5. 사용자는 결과를 확인할 수 있어야 한다.
6. 사용자는 자신의 예측 이력을 확인할 수 있어야 한다.
7. 사용자는 시간대별 실제 비트코인의 가격 변동과 투표의 과반수 결과를 차트를 통해 확인할 수 있어야 한다.
8. 사용자는 현재 포인트를 확인할 수 있어야 한다.
9. 사용자는 자신의 현재 등급을 확인할 수 있어야 한다.

## 기술 스택

- **백엔드**: Spring Boot
- **프론트엔드**: JS
- **데이터베이스**: MySQL
- **ORM**: JPA
- **캐싱**: Redis
- **인증**: JWT
- **API**: CoinGecko API

## 데이터베이스 구조

1. **User**
    - id : PK
    - email : VARCHAR
    - point: INT
    - grade : VARCHAR
    - validationDate : DATETIME
    - deviceInformation : VARCHAR
    - accountStatus : String
    - refreshToken : String
2. **Game**
    - id: PK
    - user_id: FK
    - timestamp : DATETIME
    - amount : INT
    - direction : ENUM('UP', 'DOWN')
    - outcome : ENUM('WON', 'LOST', 'PENDING')
3. **VotingResults**
    - id: PK
    - timestamp : DATETIME
    - result : ENUM('UP', 'DOWN')
4. **PriceRecord**
    - id : PK
    - timestamp : DATETIME
    - price : FLOAT
    - direction : ENUM('UP', 'DOWN')
5. **Reword**
    - id : PK
    - user_id : FK
    - bet_id : FK
    - timestamp : DATETIME
    - result : ENUM('UP', 'DOWN')
