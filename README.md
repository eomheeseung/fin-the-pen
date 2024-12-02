# fin-the-pen

project logo

![logo](https://private-user-images.githubusercontent.com/104350432/391539811-7ee04891-e30a-44b0-a10a-ada23416a789.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDI2MDAsIm5iZiI6MTczMzE0MjMwMCwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTM5ODExLTdlZTA0ODkxLWUzMGEtNDRiMC1hMTBhLWFkYTIzNDE2YTc4OS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjI1MDBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00ZWU1ZWNlODk2MDc4MmMwZGU5NDdkMzI3YTVmYzg5NDVmMDY5MTY5NmY1OGEwMmRlZDM3NjdlY2MzOTE0ZDFjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.-i9ou4WicSFyXB-yW1FnR45qDmWftJxAKoivejltz4U)

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

### Swagger
- dependency : https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
- version : **'org.springdoc:springdoc-openapi-ui:1.6.14'**

### Spring Security
- Json Web Token 사용
  - version : </br>
            **'io.jsonwebtoken:jjwt-api:0.11.5'** </br>
            **'io.jsonwebtoken:jjwt-impl:0.11.5'** </br>
            **'io.jsonwebtoken:jjwt-jackson:0.11.5'**
## Use
개발단계에서는 memory DB인 *H2 DB*를 먼저 실행시키고, Application 실행
- 만약 실행 시 DB의 Query가 넘어가지 않거나 DB에 저장되지 않는다면, *application.yml*에 들어가서 아래의 ddl-auto를 create로 바꿔주면 된다.
