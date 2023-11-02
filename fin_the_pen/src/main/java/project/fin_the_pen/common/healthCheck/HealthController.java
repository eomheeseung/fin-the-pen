package project.fin_the_pen.common.healthCheck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/alive")
public class HealthController {

    @GetMapping
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("i'm alive");
    }
}
