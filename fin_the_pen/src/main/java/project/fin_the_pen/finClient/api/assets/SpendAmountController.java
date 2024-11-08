package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.assets.service.SpendGoalAmountService;
import project.fin_the_pen.model.assets.spend.dto.SpendAmountRequestDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "지출 목표 금액", description = "지출 목표 금액 설정 및 화면")
public class SpendAmountController {
    private final SpendGoalAmountService spendGoalAmountService;
    /**
     * 지출 목표액
     */
    @Operation(summary = "지출 목표액 view", description = "지출 목표액 화면을 보여줌")
    @GetMapping("/spend-goal/view")
    public ResponseEntity<Object> viewSpendingGoal(@RequestParam("userId") String userId, @RequestParam("date") String date, HttpServletRequest request) {
        return ResponseEntity.ok().body(spendGoalAmountService.viewSpendGoalAmount(userId, date, request));
    }

    @Operation(summary = "지출 목표액 set", description = "지출 목표액 설정을 누르면 실행")
    @PostMapping("/spend-goal/set")
    public ResponseEntity<Object> setSpendingGoal(@RequestBody SpendAmountRequestDto dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(spendGoalAmountService.setSpendGoalAmount(dto, request));
    }
}
