# fin-the-pen

## Spring Boot 
* version : '2.7.7'
* SDK :  JAVA 11

## Data Base
### H2
- version : 1.4.200
- username : sa (default)
- password :
- jdbc url : jdbc:h2:tcp://localhost/~/fin_the_pen

### MySQL
- version : 8.0.3x 

## Use
개발단계에서는 memory DB인 *H2 DB*를 먼저 실행시키고, Application 실행
- 만약 실행 시 DB의 Query가 넘어가지 않거나 DB에 저장되지 않는다면, *application.yml*에 들어가서 아래의 ddl-auto를 create로 바꿔주면 된다.

![스크린샷 2023-11-16 오후 2.34.54.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fts%2F5dgs3_cj7r7906b60_c08s_h0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_FllPLx%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202023-11-16%20%EC%98%A4%ED%9B%84%202.34.54.png)

