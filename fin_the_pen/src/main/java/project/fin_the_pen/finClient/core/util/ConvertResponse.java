package project.fin_the_pen.finClient.core.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConvertResponse {
    @NotNull
    public ResponseEntity<Object> getResponseEntity(Map<String, Object> responseMap) {
        ResponseEntity<Object> responseEntity = null;

        if (responseMap.get("data").equals("error")) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return responseEntity;
        }

        responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return responseEntity;
    }
}
