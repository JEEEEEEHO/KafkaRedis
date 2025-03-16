# Redis/Kafka 이용한 대용량 트래픽 예약 및 쿠폰 발급 서비스

[서울 우언 잇츠 3기] 스터디 그룹 '발칙한 Tech Speakers' 연구 주제 결과물   
https://github.com/SeoulWomenIts-TechSpeakers/TechSpeakers
### ⚒️ 현재 동기식 서버 아키텍처

#### 1. 시퀀스 다이어그램
![img.png](img.png)
- 요청 이후 호스트에 등록한 재고를 확인함
- 요청 <= 재고 인 경우 예약 체결
- 체결 완료한 요청에 경우 쿠폰 발급 

#### 2. 발생할 수 있는 문제 
- 동시 요청의 경우 재고 업데이트 이전에 재고를 조회 
  - 동시 요청 만큼 재고가 차감되지 않고, 기존에 있는 재고로 Lost Update 문제 발생
- 쿠폰로직이 예약 로직과 같은 트랜잭션에 엮여 있음 
  - 지연 발생 가능 

### ⚒️ 대량 트래픽 개선 서버 아키텍처 
#### 1. 아키텍처 구조 계획
![비동기.drawio.png](..%2F..%2FOneDrive%2F%EB%B0%94%ED%83%95%20%ED%99%94%EB%A9%B4%2Fstudying%2FKafkaRedis%2F%EB%B9%84%EB%8F%99%EA%B8%B0.drawio.png)

#### 2. 사용한 기술 스택 
- 동시 요청 Lost Update 문제 
  - 자바 멀티 스레드+병렬의 특징을 막아주어야 함 
  - Lock 을 걸어줌
  - 비관 Lock을 사용하여 데이터에 걸 수도 있지만, 데이터 자체에 락을 걸어서 성능의 저하 문제나 deadlock 가능성이 있음
  - 또한 읽기에서는 