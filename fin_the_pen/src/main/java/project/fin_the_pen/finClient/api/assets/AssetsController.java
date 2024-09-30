package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.model.assets.service.PeriodicService;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "자산관리 및 설정", description = "저축 목표 금액, 정기 입출 금, 카테고리별 자산 설정, 로그인 후 이용가능")
public class AssetsController {

    private final PeriodicService periodicService;


    @Operation(summary = "자산 관리 홈", description = "자산관리 홈화면")
    @PostMapping("/home")
    public ResponseEntity<Object> assetsHome() {
        return ResponseEntity.ok().build();
    }




}
