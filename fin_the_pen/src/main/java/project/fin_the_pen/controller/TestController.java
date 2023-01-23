package project.fin_the_pen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {
    @GetMapping("/fin/the/pen-web/hello")
    public List<String> Hello() {
        return Arrays.asList("서버 포트는 8080", "리액트 포트는 3000");
    }
}
