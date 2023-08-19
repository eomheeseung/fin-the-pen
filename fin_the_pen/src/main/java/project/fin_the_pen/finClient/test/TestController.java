package project.fin_the_pen.finClient.test;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {
    @GetMapping("test")
    public JSONObject init(@RequestBody TestRequestDto dto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", dto.getName());
        jsonObject.put("phone_number", dto.getPhoneNumber());
        log.info((String) jsonObject.get("name"));

        return jsonObject;
    }
}
