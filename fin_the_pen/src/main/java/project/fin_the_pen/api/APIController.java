package project.fin_the_pen.api;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@Slf4j
public class APIController {
    //사용자 인증 사이트로 넘어감.
    private final StringBuffer URLstringBuffer = new StringBuffer("https://testapi.openbanking.or.kr");
    private static int length;
    private TokenIssuance tokenIssuance;

    @GetMapping("auth")
    public void userAuthorization(@RequestParam String code) {
        length = URLstringBuffer.length();

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

    @GetMapping("accountAuth")
    @ResponseBody
    public String accountAuth() throws IOException {
        URLstringBuffer.append("/v2.0/user/me");

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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("accountInquiry")
    @ResponseBody
    public String accountInquiry(@RequestParam String bankTranId,
                                 @RequestParam String fintechUseNum, @RequestParam String tranDTime) throws IOException {

        String param = "?bank_tran_id=" + bankTranId + "&fintech_use_num=" + fintechUseNum + "&tran_dtime=" + tranDTime;
        URLstringBuffer.append("/v2.0/account/balance/fin_num");

        try {
            URL url = new URL(URLstringBuffer.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + tokenIssuance.getAccessToken());

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            try (DataOutputStream output = new DataOutputStream(httpURLConnection.getOutputStream())) {
                output.writeBytes(param);
                output.flush();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            bufferedReader.close();
            extractedSB(stringBuffer);

            String response = stringBuffer.toString();
            log.info(response);
            httpURLConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
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
