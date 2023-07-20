# 지하철 노선도

## 소개

이번 미션은 지하철 노선도라는 요구사항을 가진 웹 애플리케이션을 구현하는 것 입니다.
제공되는 뼈대코드를 바탕으로 노선에 역을 등록하고 제거하는 기능을 객체지향적으로 설계하고 구현하는 것을 목표로 합니다.
이번 단계에서 구현되는 노선에 역 등록/제거 기능은 다음 단계의 요구사항인 경로 조회 기능을 위한 정보 관리 기능입니다.
비즈니스 규칙을 검증하는 테스트를 각각 구현합니다. 테스트를 이용하여 요구사항을 만족하는지를 확인합니다.
가급적 TDD로 구현합니다. 작성한 테스트를 통해 애플리케이션을 실행시키고 브라우저를 띄워서 확인하지 않아도 정상 동작 여부를 확인할 수 있습니다.

## 요구사항

### Step2

요구사항 설명에서 제공되는 추가된 요구사항을 기반으로 지하철 구간 관리 기능을 리팩터링하세요.
예외 케이스에 대한 검증도 포함하세요.

#### 변경된 스펙 - 구간 추가 제약사항 변경

- [X] 모든 기존 역에 새로운 구간을 추가할 수 있음(하행종점역 아니여도 구간 추가 가능)
    - [X] 기존 구간의 역을 기준으로 새로운 구간을 추가
        - 기존 구간 A-C에 신규 구간 A-B를 추가하는 경우 A역을 기준으로 추가
        - 기존 구간과 신규 구간이 모두 같을 순 없음(아래 예외사항에 기재됨)
        - 결과로 A-B, B-C 구간이 생김
    - [X] 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정
- [X] 노선 조회시 응답이 상행부터 하행 순서대로 조회되야함
    - 상행 종점이 상행역인 구간을 먼저 찾는다.
    - 그 다음, 해당 구간의 하행역이 상행역인 다른 구간을 찾는다.
    - 2번을 반복하다가 하행 종점역을 찾으면 조회를 멈춘다.
- [X] 예외
    - [X] 역사이에 역 등록시 구간이 기존 구간보다 크거나 같으면 등록 불가
    - [X] 상,하행역이 모두 등록되어있으면 등록 불가
    - [X] 상,하행역이 모두 등록 안되어있으면 등록 불가

### 프론트엔드 코드와 동기화

- [X] 노선을 생성 시 첫 구간도 같이 생성하도록 변경
- [X] 노선 상세 조회시 노선에 포함된 역들을 순서대로 반환

### Step 1

요구사항 설명에서 제공되는 요구사항을 기반으로 지하철 구간 관리 기능을 구현하세요.
예외 케이스에 대한 검증도 포함하세요.

#### 구간 등록 기능

- [X] '구간' 있어야함
- [X] 구간에는 노선, 상행역과 하행역과 거리가 있어야한다.
- [X] 지하철 노선에 구간을 등록하는 기능을 구현
- [X] 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
    - [X] 노선에 하행 종점역을 구하는 Dao method 를 구현한다.
    - [X] 하행 종점역과 새로운 구간의 상행역이 다르면 예외를 던진다.
- [X] 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
    - [X] 구간 테이블에서 노선에 해당하는 하행역이 존재하는지 확인하는 Dao method 를 구현한다.
    - [X] 새로운 구간 하행역이 기존 노선에 존재하면 예외를 던진다.
- [X] 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
- 구간 등록 request

```
POST /lines/1/sections HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8
host: localhost:52165

{
"downStationId": "4",
"upStationId": "2",
"distance": 10
}
```

#### 구간 제거 기능

- [x] 지하철 노선에 구간을 제거하는 기능 구현
- [x] 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
- [x] 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
- [x] 새로운 구간 제거시 위 조건에 부합하지 않는 경우 에러 처리한다.
- 지하철 구간 삭제 request

```
- DELETE /lines/1/sections?stationId=2 HTTP/1.1
accept: */*
host: localhost:52165
```

#### 구간 관리 기능의 예외 케이스를 고려하기

- [x] 구간 등록과 제거 기능의 예외케이스들에 대한 시나리오를 정의
- [x] 인수 테스트를 작성하고 이를 만족시키는 기능을 구현

#### 추가로하면 좋을 것들

- [ ] 서비스에서 로직 처리하기
- [ ] 커스텀 예외 정의
- [ ] 예외 핸들러 정의
