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
