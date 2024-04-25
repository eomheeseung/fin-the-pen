package project.fin_the_pen.finClient.dev;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "dev용",description = "정기 입출금액 개별 수정")
public class DevController {
    private final DevService service;

    @Operation(description = "테스트용",summary = "test")
    @PostMapping("/modify/dev")
    public ResponseEntity<HttpStatus> devModify(@RequestBody DevModifyDto dto) {
        HttpStatus modify = service.modify(dto);
        return ResponseEntity.status(modify).build();
    }
}
