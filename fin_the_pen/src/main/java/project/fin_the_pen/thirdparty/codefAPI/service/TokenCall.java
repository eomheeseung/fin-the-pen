package project.fin_the_pen.thirdparty.codefAPI.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;

public class TokenCall {
    private static final ObjectMapper mapper = new ObjectMapper();

    protected static HashMap<String, Object> publishToken(String clientId, String clientSecret) {
        BufferedReader br = null;
        String oauthDomain = "https://oauth.codef.io";
        String getToken = "/oauth/token";

        try {
            // HTTP 요청을 위한 URL 오브젝트 생성
            URL url = new URL(oauthDomain + getToken);
            String params = "grant_type=client_credentials&scope=read";    // Oauth2.0 사용자 자격증명 방식(client_credentials) 토큰 요청 설정

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 클라이언트아이디, 시크릿코드 Base64 인코딩
            String auth = clientId + ":" + clientSecret;
            byte[] authEncBytes = Base64.encodeBase64(auth.getBytes());
            String authStringEnc = new String(authEncBytes);
            String authHeader = "Basic " + authStringEnc;

            con.setRequestProperty("Authorization", authHeader);
            con.setDoInput(true);
            con.setDoOutput(true);

            // 리퀘스트 바디 전송
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            // 응답 코드 확인
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {    // 정상 응답
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {     // 에러 발생
                return null;
            }

            // 응답 바디 read
            String inputLine;
            StringBuffer responseStr = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                responseStr.append(inputLine);
            }
            br.close();

            HashMap<String, Object> tokenMap = mapper.readValue(URLDecoder.decode(responseStr.toString(), "UTF-8"),
                    new TypeReference<HashMap<String, Object>>() {
                    });
            return tokenMap;
        } catch (Exception e) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
