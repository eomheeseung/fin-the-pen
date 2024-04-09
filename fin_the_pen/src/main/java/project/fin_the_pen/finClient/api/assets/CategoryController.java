package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.assets.category.dto.CategoryGoalRequestDto;
import project.fin_the_pen.model.assets.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "카테고리별 자산 설정", description = "카테고리별 자산설정, 카테고리별로의 지출 목표액 설정 및 화면")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리별 자산 설정
     *
     * @param userId
     * @param request
     * @return
     */
    @Operation(summary = "카테고리별 자산 설정 view", description = "카테고리별 자산 설정 화면, 지출목표액과 카테고리별 지출 목표를 설정해야지 보입니다.")
    @GetMapping("/category-amount")
    public ResponseEntity<Object> viewCategoryAmount(@RequestParam("userId") String userId, @RequestParam("date") String date, HttpServletRequest request) {
        Map<Object, Object> objectObjectMap =
                categoryService.viewAmount(userId, date, request);
        return ResponseEntity.ok().body(objectObjectMap);
    }

    @Operation(summary = "카테고리별 지출 목표액 설정", description = "카테고리별 지출 목표액 설정")
    @PostMapping("/category-amount/set")
    public ResponseEntity<Object> setCategoryAmount(@RequestBody CategoryGoalRequestDto dto, HttpServletRequest request) {
        boolean flag = categoryService.setAmount(dto, request);
        return ResponseEntity.ok().body(flag);
    }

    @Operation(summary = "카테고리별 지출 목표액 삭제(전체)", description = "카테고리별 지출 목표액 삭제")
    @DeleteMapping("/category-amount/delete")
    public ResponseEntity<Object> deleteCategoryAmount(@RequestParam("userId") String userId,
                                                       @RequestParam("date") String date,
                                                       HttpServletRequest request) {
        boolean flag = categoryService.deleteAmount(userId, date, request);
        return ResponseEntity.ok().body(flag);
    }
}
