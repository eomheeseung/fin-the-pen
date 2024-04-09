package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.assets.periodic.dto.PeriodAmountDeleteRequestDto;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicAmountRequestDto;
import project.fin_the_pen.model.assets.service.PeriodicService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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







    /**
     * 정기 입출 금액
     *
     * @param userId
     * @param request
     * @return
     */
    @Operation(summary = "정기 입출금액 설정 view,", description = "정기 입출금액 설정")
    @GetMapping("/period-amount")
    public ResponseEntity<Object> viewPeriodicAmount(@RequestParam("userId") String userId, HttpServletRequest request) {
        HashMap<Object, Object> responseMap = periodicService.viewPeriodAmount(userId, request);

        return ResponseEntity.ok().body(responseMap);
    }

    @Operation(summary = "정기 입출금액 설정 page,", description = "정기 입출금액 설정 및 수정")
    @PostMapping("/period-amount/set")
    public ResponseEntity<Object> setPeriodicAmount(@RequestBody PeriodicAmountRequestDto dto, HttpServletRequest request) {
        boolean flag = periodicService.setPeriodicAmount(dto, request);

        return ResponseEntity.ok().body(flag);
    }

    @Operation(summary = "정기 입출금액 삭제", description = "정기 입출 금액 삭제")
    @DeleteMapping("/periond-amount/delete")
    public ResponseEntity<Object> deletePeriodAmount(@RequestBody PeriodAmountDeleteRequestDto dto, HttpServletRequest request) {
        boolean flag = periodicService.deletePeriodAmount(dto, request);

        return ResponseEntity.ok().body(flag);
    }



    /*@Operation(summary = "카테고릴별 자산 설정 -> 자산 세부 설정", description = "자산 세부 설정")
    @PostMapping("/category-amount/detail")
    public ResponseEntity<Object> setDetailCategory(@RequestBody DetailCategoryRequestDto dto, HttpServletRequest request) {
        categoryService.setCategory(dto, request);
        return ResponseEntity.ok().build();
    }*/
}
