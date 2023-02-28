package project.fin_the_pen.api;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import project.fin_the_pen.api.service.APIService;

import java.io.IOException;

@RestClientTest(value = APIService.class)
public class ApiServiceMockWebServerTest {
    @Autowired
    private APIService apiService;
    private NoAuthTokenIssuance noAuthTokenIssuance;
    @Autowired
    public static MockWebServer mockWebServer;

    private StringBuffer URLStringBuffer = new StringBuffer("https://testapi.openbanking.or.kr");

    @BeforeAll
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public void shutdown() throws IOException {
        mockWebServer.shutdown();
    }
    @Test
    @DisplayName(value = "인증없는 사용자 토큰 받아오기")
    public void noUserAuth() {
        mockWebServer.url(URLStringBuffer.append("/oauth/2.0/token").toString());
//        mockWebServer.enqueue(new MockResponse().setBody());
    }
}
