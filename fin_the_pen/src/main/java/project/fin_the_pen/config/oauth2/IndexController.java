package project.fin_the_pen.config.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/welcome")
    public String welCome() {
        return "welcome";
    }
}
