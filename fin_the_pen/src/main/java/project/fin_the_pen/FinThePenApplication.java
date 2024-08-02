package project.fin_the_pen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import project.fin_the_pen.config.oauth2.kakao.KakaoClientProperties;

@SpringBootApplication
@EnableConfigurationProperties(KakaoClientProperties.class)
public class FinThePenApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinThePenApplication.class, args);
	}

}
