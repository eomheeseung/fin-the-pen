package project.fin_the_pen.finClient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.service.AssetsService;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AssetsController {
    private final AssetsService assetsService;

    @PostMapping("/assets/find/schedule")
    public JSONObject assetsPrintSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        return assetsService.assetsPrintSchedule(map.get("date"), map.get("user_id"));
    }
}
