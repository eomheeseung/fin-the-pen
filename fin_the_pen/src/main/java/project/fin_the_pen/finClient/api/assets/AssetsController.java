package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.assets.category.dto.DetailCategoryRequestDto;
import project.fin_the_pen.model.assets.periodic.dto.PeriodAmountDeleteRequestDto;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicAmountRequestDto;
import project.fin_the_pen.model.assets.saving.dto.personal.PersonalRequestDto;
import project.fin_the_pen.model.assets.saving.dto.personal.PersonalResponseDto;
import project.fin_the_pen.model.assets.saving.dto.targetAmount.TargetAmountRequestDto;
import project.fin_the_pen.model.assets.saving.dto.targetAmount.TargetAmountResponseDto;
import project.fin_the_pen.model.assets.service.AssetsService;
import project.fin_the_pen.model.assets.service.CategoryService;
import project.fin_the_pen.model.assets.service.PeriodicService;
import project.fin_the_pen.model.report.dto.ExpenditureRequestDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "자산관리 및 설정", description = "저축 목표 금액, 정기 입출 금, 카테고리별 자산 설정, 로그인 후 이용가능")
public class AssetsController {
    private final AssetsService assetsService;
    private final PeriodicService periodicService;
    private final CategoryService categoryService;


    @Operation(summary = "자산 관리 홈", description = "자산관리 홈화면")
    @PostMapping("/home")
    public ResponseEntity<Object> assetsHome() {
        return ResponseEntity.ok().build();
    }


    /**
     * 저축 목표액 설정 / 개인적 목표 설정
     *
     * @param userId
     * @param request
     * @return
     */
    /*
    TODO
     저축 목표 설정 / person goal에서 상세
     */
    @Operation(summary = "저축 목표 금액 화면", description = "저축 목표 금액 view")
    @GetMapping("/target-amount")
    public ResponseEntity<Object> targetAmountView(@RequestParam("userId") String userId, HttpServletRequest request) {
        TargetAmountResponseDto responseDto = assetsService.viewTargetAmount(userId, request);
        PersonalResponseDto personalResponseDto = assetsService.viewPersonalGoal(userId, request);

        HashMap<String, Object> responseMap = new HashMap<>();

        responseMap.put("goal_amount", responseDto);
        responseMap.put("personal_goal", personalResponseDto);

        return ResponseEntity.ok().body(responseMap);
    }


    /**
     * notice!!
     * front에서 설정하고 /target-amount로 redircet해야 함..
     *
     * @param dto
     * @param request
     * @return
     */
    @Operation(summary = "저축 목표액 실제 설정", description = "저축 목표액 setting")
    @PostMapping("/target-amount/set")
    public ResponseEntity<Object> setTargetAmount(@RequestBody TargetAmountRequestDto dto, HttpServletRequest request) {
        boolean flag = assetsService.setTargetAmount(dto, request);
        return ResponseEntity.ok().body(flag);
    }

    @Operation(summary = "개인 목표 설정 및 수정", description = "개인 목표 설정")
    @PostMapping("/personal-goal")
    public ResponseEntity<Object> personalGoalSet(@RequestBody PersonalRequestDto dto, HttpServletRequest request) {
        boolean flag = assetsService.personalGoalSet(dto, request);

        return ResponseEntity.ok().body(flag);
    }

    @Operation(summary = "저축 목표액 초기화", description = "저축 목표액 초기화")
    @DeleteMapping("/target-amount/set")
    public ResponseEntity<Object> initTargetAmount(@RequestParam("userId") String userId, HttpServletRequest request) {
        boolean flag = assetsService.initTargetAmount(userId, request);

        return ResponseEntity.ok().body(flag);
    }

    /**
     * 정기 입출 금액
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

    /**
     * 카테고리별 자산 설정
     * TODO response 만들고 test
     * @param userId
     * @param request
     * @return
     */
    @Operation(summary = "카테고리별 자산 설정 view", description = "카테고리별 자산 설정 화면")
    @GetMapping("/category-amount")
    public ResponseEntity<Object> viewCategoryAmount(@RequestParam("userId") String userId, HttpServletRequest request) {

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "카테고리별 자산 설정 -> 지출 목표액 설정", description = "지출 목표액 설정")
    @PostMapping("/category-amount/expenditure")
    public ResponseEntity<Object> setCategoryAmount(@RequestBody ExpenditureRequestDTO dto, HttpServletRequest request) {
        boolean flag = categoryService.setAmount(dto, request);
        return ResponseEntity.ok().body(flag);
    }

    @Operation(summary = "카테고릴별 자산 설정 -> 자산 세부 설정", description = "자산 세부 설정")
    @PostMapping("/category-amount/detail")
    public ResponseEntity<Object> setDetailCategory(@RequestBody DetailCategoryRequestDto dto, HttpServletRequest request) {
        categoryService.setCategory(dto, request);
        return ResponseEntity.ok().build();
    }
}
