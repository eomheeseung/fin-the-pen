package project.fin_the_pen.api.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
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

            noAuthTokenIssuance = new NoAuthTokenIssuance(responseJson.get("access_token").toString(),
                    responseJson.get("token_type").toString(), responseJson.get("expires_in").toString(),
                    responseJson.get("scope").toString(), responseJson.get("client_use_code").toString());

            log.info(noAuthTokenIssuance.getAccessToken());
            httpURLConnection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            extractedSB(URLStringBuffer);
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
            String param = "code=" + code + "&client_id=" + "24ffbbc3-fd31-426f-80ac-ec22e36ce10d" +
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

                tokenIssuance = new TokenIssuance(responseJson.get("access_token").toString(),
                        responseJson.get("token_type").toString(), responseJson.get("refresh_token").toString(),
                        responseJson.get("expires_in").toString(), responseJson.get("scope").toString(),
                        responseJson.get("user_seq_no").toString());

                System.out.println(responseJson);
                log.info(tokenIssuance.getAccessToken());
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
     *
     * @param URStringBuffer
     * @return
     */
    public String accountAuthLogic(StringBuffer URStringBuffer) {
        try {
            URStringBuffer.append("?user_seq_no=").append(tokenIssuance.getUserSeqNo());
            URL url = new URL(URStringBuffer.toString());
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
            extractedSB(URStringBuffer);

            String response = stringBuffer.toString();
            log.info(response);
            httpURLConnection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.2.3 등록 계좌 조회
     *
     * @param URLstringBuffer
     * @return
     */
    public String accountListLogic(StringBuffer URLstringBuffer) {
        String param = "?user_seq_no=" + tokenIssuance.getUserSeqNo() + "&include_cancel_yn=" + "Y" + "&sort_order=" + "D";
        try {
            URLstringBuffer.append("/v2.0/account/list").append(param);
            URL url = new URL(URLstringBuffer.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String readLine;

            while ((readLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(readLine);
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
     * 2.2.4 계좌 정보 변경 : 오픈뱅킹센터에 등록된 계좌의 별명을 변경
     * 핀테크 이용번호를 어떻게 동적으로 바인딩할 것인가?
     *
     * @param URLStringBuffer
     * @return
     */
    public JSONObject accountUpdateLogic(StringBuffer URLStringBuffer) {
        URLStringBuffer.append("/v2.0/account/update_info");

        try {
            URL url = new URL(URLStringBuffer.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());
            httpURLConnection.connect();

            JSONObject requestJson = new JSONObject();

            // 여기 핀테크 번호를 동적바인딩이 필요함. => 파라미터를 어디서 끌어올지...
            requestJson.put("fintech_use_num", "120230024688951003826191");
            requestJson.put("account_alias", "updateTest");

            try {
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.write(requestJson.toString().getBytes());
                dataOutputStream.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONObject responseJson = new JSONObject(stringBuilder.toString());
            log.info(responseJson.toString());
            bufferedReader.close();
            httpURLConnection.disconnect();
            extractedSB(URLStringBuffer);

            return responseJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2.2.6 계좌 정보 조회 : 등록된 계좌의 정보를 조회, 해당 API는 참가으느행에 등록된 내역을 조회
     * test 해봐야 함.
     *
     * @param URLStringBuffer
     * @return
     */
    public JSONObject accountInfoLogic(StringBuffer URLStringBuffer) {
        StringBuffer bankTranId = new StringBuffer("M202300246U0000000");
        int random = new Random().nextInt(30) + 10;
        bankTranId.append(random);

        URLStringBuffer.append("/v2.0/account/info");

        try {
            URL url = new URL(URLStringBuffer.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            JSONObject requestJson = new JSONObject();
            requestJson.put("bank_tran_id", bankTranId);
            requestJson.put("user_seq_no", tokenIssuance.getUserSeqNo());
            requestJson.put("bank_code_std", "097");
            requestJson.put("account_num", "123412341234");
            requestJson.put("account_seq", "124");
            requestJson.put("scope", "inquiry");

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(requestJson.toString().getBytes());
            dataOutputStream.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONObject responseJson = new JSONObject(stringBuilder.toString());
            log.info(responseJson.toString());
            httpURLConnection.disconnect();
            bufferedReader.close();
            extractedSB(URLStringBuffer);

            return responseJson;
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
        URLstringBuffer.append("/v2.0/account/balance/fin_num").append(param);

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


    private static void extractedSB(StringBuffer URLStringBuffer) {
        String substring = URLStringBuffer.substring(0, length);
        URLStringBuffer.setLength(0);
        URLStringBuffer.append(substring);
        log.info("복구된 url={}", URLStringBuffer);
    }
}
