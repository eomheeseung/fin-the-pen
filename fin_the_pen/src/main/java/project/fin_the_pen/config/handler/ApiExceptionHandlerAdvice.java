package project.fin_the_pen.config.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.fin_the_pen.finClient.core.error.dto.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "project.fin_the_pen")
public class ApiExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> badRequestException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .status(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .status(HttpStatus.FORBIDDEN.value())
                .build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build());
    }
}
