package project.fin_the_pen.api.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import project.fin_the_pen.api.Issuance;
import project.fin_the_pen.api.NoAuthTokenIssuance;
import project.fin_the_pen.api.TokenIssuance;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@Component
@Getter
@Slf4j
public class APIService {
    private JSONParser jsonParser = null;
    private static int length;
    private TokenIssuance tokenIssuance;
    private NoAuthTokenIssuance noAuthTokenIssuance;

    private Issuance issuance;


    //TODO 파싱 해야 함.
    public void init(String response) {
        try {
//            Account saveAccount = objectMapper.readValue(response, Account.class);
            jsonParser = new JSONParser(response);
            JSONObject jsonObject = (JSONObject) jsonParser.parse();
            log.info((String) jsonObject.get("balance_amt"));
//            log.info(String.valueOf(saveAccount.getBalance_amt()));
        } /*catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } */ catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.1.2 이용기관 토큰 발급 2-legged
     *
     * @param URLStringBuffer
     * @param length
     */
    public void noUserAuth(StringBuffer URLStringBuffer, int length) {
        this.length = length;
        JSONObject responseJson = null;
        HttpURLConnection httpURLConnection = null;
        String param = "?client_id=" + "24ffbbc3-fd31-426f-80ac-ec22e36ce10d" +
                "&client_secret=" + "80d52d5a-fd08-4248-aaf0-b21247b20c53"
                + "&scope=" + "oob"
                + "&grant_type=" + "client_credentials";

        try {
            URLStringBuffer.append("/oauth/2.0/token").append(param);
            URL url = new URL(URLStringBuffer.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }

            responseJson = new JSONObject(stringBuilder.toString());
            log.info(stringBuilder.toString());

            issuance = new NoAuthTokenIssuance(responseJson.get("access_token").toString(),
                    responseJson.get("token_type").toString(), responseJson.get("expires_in").toString(),
                    responseJson.get("scope").toString(), responseJson.get("client_use_code").toString());

            System.out.println(responseJson);
            log.info(((NoAuthTokenIssuance)issuance).getAccessToken());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            extractedSB(URLStringBuffer);
            httpURLConnection.disconnect();
        }
    }

    /**
     * 2.1.2 토큰발급 3-legged
     *
     * @param code
     * @param length
     */
    public void userAuthLogic(String code, int length) {
        this.length = length;

        HttpURLConnection httpURLConnection = null;
        JSONObject responseJson = null;

        // application/x-www-form-urlencoded인 경우
        // key=value&key=value형태
        try {
            URL url = new URL("https://testapi.openbanking.or.kr/oauth/2.0/token");
            String param = "?code=" + code + "&client_id=" + "24ffbbc3-fd31-426f-80ac-ec22e36ce10d" +
                    "&client_secret=" + "80d52d5a-fd08-4248-aaf0-b21247b20c53"
                    + "&redirect_uri=" + "http://localhost:63342/fin-the-pen/fin_the_pen.main/resource/home"
                    + "&grant_type=" + "authorization_code";

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            // application/x-www-form-urlencoded, application/json 등
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 응답받을 데이터가 있는 경우 true
            httpURLConnection.setDoInput(true);

            // 요청시 데이터를 보내야 하는 경우 true
            httpURLConnection.setDoOutput(true);

            try (DataOutputStream output = new DataOutputStream(httpURLConnection.getOutputStream())) {
                output.writeBytes(param);
                output.flush();
            }

            JSONObject commands = new JSONObject();

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 400 || responseCode == 401 || responseCode == 500) {
                System.out.println(responseCode + " Error!");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                responseJson = new JSONObject(sb.toString());

                issuance = new TokenIssuance(responseJson.get("access_token").toString(),
                        responseJson.get("token_type").toString(), responseJson.get("refresh_token").toString(),
                        responseJson.get("expires_in").toString(), responseJson.get("scope").toString(),
                        responseJson.get("user_seq_no").toString());

                System.out.println(responseJson);
                log.info(((TokenIssuance)issuance).getAccessToken());
                httpURLConnection.disconnect();
            }
        } catch (JSONException e) {
            System.out.println("not JSON Format response");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2.2.1 사용자 계좌 조회
     * @param URLstringBuffer
     * @return
     */
    public String accountAuthLogic(StringBuffer URLstringBuffer) {
        try {
            URLstringBuffer.append("?user_seq_no=").append(tokenIssuance.getUserSeqNo());
            URL url = new URL(URLstringBuffer.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());
            log.info(tokenIssuance.getAccessToken());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }

            bufferedReader.close();
            extractedSB(URLstringBuffer);

            String response = stringBuffer.toString();
            log.info(response);
            httpURLConnection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.3.1 계좌 잔액 조회
     *
     * @param URLstringBuffer
     * @return
     */
    public String accountInquiryLogic(StringBuffer URLstringBuffer) {
        StringBuffer bankTranId = new StringBuffer("M202300246U0000000");
        int random = new Random().nextInt(30) + 10;
        bankTranId.append(random);
        String fintechUseNum = "120230024688951003826191";
        String tranDTime = "20230205170942";

        String param = "?bank_tran_id=" + bankTranId + "&fintech_use_num=" + fintechUseNum + "&tran_dtime=" + tranDTime;
        URLstringBuffer.append("/v2.0/account/balance/fin_num" + param);

        try {
            URL url = new URL(URLstringBuffer.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            /*try (DataOutputStream output = new DataOutputStream(httpURLConnection.getOutputStream())) {
                output.writeBytes(param);
                output.flush();
            }*/

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }

            bufferedReader.close();
            extractedSB(URLstringBuffer);

            String response = stringBuffer.toString();
            // response를 역직렬화
//            apiService.init(response);

            log.info(response);
            httpURLConnection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.3.2 거래 내역 조회
     * TODO 인증기관 코드가 098로 뜸...
     *
     * @param URLstringBuffer
     * @return
     */
    public String accountCheckLogic(StringBuffer URLstringBuffer) {
        StringBuffer bankTranId = new StringBuffer("M202300246U0000000");
        int random = new Random().nextInt(30) + 10;
        bankTranId.append(random);
        String fintechUseNum = "120230024688951003826191";
        String inquiryType = "A";
        String inquiryBase = "D";
        String fromDate = "20230218";
        String toDate = "20230227";
        String sortOder = "D";
        String tranDTime = "20230205170942";

        String param = "?bank_tran_id=" + bankTranId + "&fintech_use_num=" + fintechUseNum +
                "&inquiry_type=" + inquiryType + "&inquiry_base=" + inquiryBase + "&from_date=" + fromDate +
                "&to_date=" + toDate + "&sort_order=" + sortOder + "&tran_dtime=" + tranDTime;

        URLstringBuffer.append("/v2.0/account/transaction_list/fin_num").append(param);

        try {
            URL url = new URL(URLstringBuffer.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            /*try (DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream())) {
                dataOutputStream.writeBytes(param);
                dataOutputStream.flush();
            }*/

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;

            if ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }

            bufferedReader.close();
            extractedSB(URLstringBuffer);

            String response = stringBuffer.toString();
            log.info(response);
            urlConnection.disconnect();

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void extractedSB(StringBuffer stringBuffer) {
        String substring = stringBuffer.substring(0, length);
        stringBuffer.setLength(0);
        stringBuffer.append(substring);
        log.info(stringBuffer.toString());
    }
}
