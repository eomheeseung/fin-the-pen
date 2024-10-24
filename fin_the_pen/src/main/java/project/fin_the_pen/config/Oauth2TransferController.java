package project.fin_the_pen.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class Oauth2TransferController {

    @GetMapping("/login/naver")
    public void naverLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/oauth2/authorization/naver");
    }
}
