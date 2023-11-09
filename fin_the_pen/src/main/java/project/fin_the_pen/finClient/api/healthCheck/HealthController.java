package project.fin_the_pen.finClient.api.healthCheck;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "헬스 check", description = "서버가 살아있는지 확인 하는 api")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/alive")
public class HealthController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공"
            )
    })
    @Operation(summary = "health check", description = "서버 살아있는지 확인 하는 API")
    @GetMapping
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("i'm alive");
    }
}
