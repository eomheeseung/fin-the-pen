# fin-the-pen

<img src="https://private-user-images.githubusercontent.com/104350432/391539811-7ee04891-e30a-44b0-a10a-ada23416a789.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDI2MDAsIm5iZiI6MTczMzE0MjMwMCwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTM5ODExLTdlZTA0ODkxLWUzMGEtNDRiMC1hMTBhLWFkYTIzNDE2YTc4OS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjI1MDBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00ZWU1ZWNlODk2MDc4MmMwZGU5NDdkMzI3YTVmYzg5NDVmMDY5MTY5NmY1OGEwMmRlZDM3NjdlY2MzOTE0ZDFjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.-i9ou4WicSFyXB-yW1FnR45qDmWftJxAKoivejltz4U" alt="login Image" width="300" />


### Project Description
- 가계부와 일정의 기능을 합친 어플
- 일정에 따른 자산(수입, 지출)을 구분해서 넣는 기능
  - 일정 저장
  1. 단건저장
  2. **"일"** 단위 반복 저장
  3. **"주"** 단위 반복 저장
  4. **"월"** 단위 반복저장
  5. **"년"** 단위 반복 저장
  
  - 일정 등록시 반복 설정가능
    - 반복 default -> 설정한 날부터 조건에 따른 50회
    - 사용자의 지정횟수만큼 반복 (front에서 100회로 max 지정)
    - 지정한 종료날짜까지 일정을 반복
- 사용자가 설정한 월지출목표 금액, 카테고리별 목표 지출액에 따라 계산 
- 카테고리별로 묶여 수입, 지출에 관한 계산 기능
- 1달전, 2달전의 지출에 관한 리포트 기능
- 템플릿을 사용해서 일정에 대한 관리를 쉽게 가능
- 일정 수정, 삭제 기능
- 템플릿 수정, 삭제 기능

### login
<img src="https://private-user-images.githubusercontent.com/104350432/391546800-24f57ba2-d017-4d51-bae9-0587fc55f5c2.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDM3OTQsIm5iZiI6MTczMzE0MzQ5NCwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTQ2ODAwLTI0ZjU3YmEyLWQwMTctNGQ1MS1iYWU5LTA1ODdmYzU1ZjVjMi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjQ0NTRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT02ZTlhY2EzNGJiZWRmNDY0OWIzMmJhYzhjMGRjYWUxMzgyODk0NDBkNDY5ZTNkMGZiOGIyN2FjYzA0NjVmNWZmJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.Rvy1b25Sz0zPf6BQ17odvwqMMcaige3TXSiTG5V8CAs" alt="login Image" width="300" />
</br>
application 자체 로그인과 kakao, naver socail login 구현

### main page
<img src="https://private-user-images.githubusercontent.com/104350432/391549014-a34c9069-744f-4e06-bd58-11f04c7c8234.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDQxMDAsIm5iZiI6MTczMzE0MzgwMCwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTQ5MDE0LWEzNGM5MDY5LTc0NGYtNGUwNi1iZDU4LTExZjA0YzdjODIzNC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjUwMDBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1iNjlkYzYyYmY0NTU1ZGI2NDViYWYyMWNiMTEwYTA0OTRhYTRkMjFjNDc5MWIzMjBkNzljZTViZDU5YmYzMjY5JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.mLKaMuIiGZ1V179qdO0ZmH2SfSi5yU1ed1pEfQ0TpQ8" alt="login Image" width="300" />

캘린더가 표시되고, 홈, 리포트, 자산관리, 설정 탭이 아래에 존재</br>
"+" 버튼을 사용하여 일정을 등록가능 (급여를 추가한 모습, 금액 상단에 +, -를 보고 수입인지 지출인지 판단가능)</br>
<img src="https://private-user-images.githubusercontent.com/104350432/391550614-ddfd5806-3d76-49f9-84fa-6acc9c05f9b9.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDQzNDQsIm5iZiI6MTczMzE0NDA0NCwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTUwNjE0LWRkZmQ1ODA2LTNkNzYtNDlmOS04NGZhLTZhY2M5YzA1ZjliOS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjU0MDRaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT01ZGRiZDUyNzM3YTBjN2I3ZTU3MGZhNzQ2ZWMxYzZkYzEyZmZiM2RlYjAwNmYxZGYyMjYxMDBhYmVkMDk5ZTZlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.nAkRhAnPI95bLtjUYiVPbfKPkamRNr3W2rAYnCAgEkQ" alt="login Image" width="300" />
</br>
계산 후 상단의 금액이 바뀌게 된다.
<img src="https://private-user-images.githubusercontent.com/104350432/391551080-82f5ddf3-3c1e-4470-984b-dd36dea971a3.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDQ0MzUsIm5iZiI6MTczMzE0NDEzNSwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTUxMDgwLTgyZjVkZGYzLTNjMWUtNDQ3MC05ODRiLWRkMzZkZWE5NzFhMy5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjU1MzVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT03MDYwNzA3NjY4MDU2NTMxNmYyZDY5ZjgxNDY2NjlhZWZhZWQ1ZTU5N2Q3MzUyYTMzYjFkMmUxMTUwYjEzOThkJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.J6gFNX1XeOct-D08v7VaJIoPcIEwOO4EzqVWfcT_-8E" alt="login Image" width="300" />

### report탭
위에서 설정한 예시로 리포트탭을 확인하면, </br>
지출에 관한 일정이 **교통수단**이라는 카테고리로 되어 있으므로 리포트탭에서도 교통수단이 100%인 것을 확인할 수 있다.
<img src="https://private-user-images.githubusercontent.com/104350432/391551498-bd8f9376-49c9-4571-9d55-614b09a0c92d.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDQ1MjEsIm5iZiI6MTczMzE0NDIyMSwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTUxNDk4LWJkOGY5Mzc2LTQ5YzktNDU3MS05ZDU1LTYxNGIwOWEwYzkyZC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMjU3MDFaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT04YjkzNTFiYmRjOTc1ODFmYzYzNTE4ZmZhY2FhNjU4ZWM5YjlmOGFkMmRiNWI3YWQ4ZTliNmI2NzllNGZiOTA2JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.IhiCoLtDETwsET8MJcl3EHRx6Dx2xN6Qgna2ltfEseo" alt="login Image" width="300" />

</br>
아래와 같이 보험료납부에 관한 일정을 추가하게 되면, 리포트에서는 원그래프를 사용하여, </br>
지출에 대한 퍼센트가 계산되서 보여진다.
<div style="display: flex; justify-content: center; gap: 10px;">
  <img src="https://private-user-images.githubusercontent.com/104350432/391552436-688ca253-7c95-46c3-a9df-8e838ce977d4.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDQ3MTAsIm5iZiI6MTczMzE0NDQxMCwicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTUyNDM2LTY4OGNhMjUzLTdjOTUtNDZjMy1hOWRmLThlODM4Y2U5NzdkNC5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMzAwMTBaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT05ODhmYmJjNmY3MjgzMDcyZTI4NTBjMTJlYjFkNWVkZmNlMDkzMWNmYTEyMzBjNTk1MjFiZTM4ODBkZDNjMzcyJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.hgKD815TGerKMIuC9Y6IX--I-m792IoKtDgUP_2zxQk" alt="login Image" width="300" />
  <img src="https://private-user-images.githubusercontent.com/104350432/391553721-17d53b6b-dd3f-481f-802f-43ad546461c7.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MzMxNDQ5NTcsIm5iZiI6MTczMzE0NDY1NywicGF0aCI6Ii8xMDQzNTA0MzIvMzkxNTUzNzIxLTE3ZDUzYjZiLWRkM2YtNDgxZi04MDJmLTQzYWQ1NDY0NjFjNy5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQxMjAyJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MTIwMlQxMzA0MTdaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT00MjcyNmRlZTg0YWJmMjBhYTg3NWM0M2FmZWYzMmY5MGI1Yjk0MWVjODk5MjcyZTM4Mzg1ZmQwODk4ZmEyMjhhJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.e1i1Ksu6SYde9My8WfMrhuUyA1mZi3lKzNjTElUBKN4" alt="login Image" width="300" />
</div>


## Spring Boot 
* version : '2.7.7'
* SDK :  JAVA 11
* JPA
* oauth2 및 application 자체 login 가능
* 



## Database
### H2
- version : 1.4.200
- username : sa (default)
- password :
- jdbc url : jdbc:h2:tcp://localhost/~/fin_the_pen

### MySQL
- version : 8.0.3x 

## Swagger
- dependency : https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
- version : **'org.springdoc:springdoc-openapi-ui:1.6.14'**

## Spring Security
- Json Web Token 사용
  - version : </br>
            **'io.jsonwebtoken:jjwt-api:0.11.5'** </br>
            **'io.jsonwebtoken:jjwt-impl:0.11.5'** </br>
            **'io.jsonwebtoken:jjwt-jackson:0.11.5'**
## Use
개발단계에서는 memory DB인 *H2 DB*를 먼저 실행시키고, Application 실행
- 만약 실행 시 DB의 Query가 넘어가지 않거나 DB에 저장되지 않는다면, *application.yml*에 들어가서 아래의 ddl-auto를 create로 바꿔주면 된다.
