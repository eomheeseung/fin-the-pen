package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.assets.saving.dto.personal.PersonalRequestDto;
import project.fin_the_pen.model.assets.saving.dto.personal.PersonalResponseDto;
import project.fin_the_pen.model.assets.saving.dto.savingGoal.TargetAmountRequestDto;
import project.fin_the_pen.model.assets.saving.dto.savingGoal.TargetAmountResponseDto;
import project.fin_the_pen.model.assets.service.AssetsService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "저축 목표액 ", description = "저축 목표 금액 및 개인 목표 설정 ")
public class SavingAmountController {

    private final AssetsService assetsService;
    /**
     * 저축 목표액 home
     *
     * @param userId
     * @param request
     * @return
     */
    /*
    TODO
     저축 목표 설정 / person goal에서 상세
     */
    @Operation(summary = "저축 목표 금액 화면 (저축 목표 home)", description = "저축 목표 금액 view")
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
}
