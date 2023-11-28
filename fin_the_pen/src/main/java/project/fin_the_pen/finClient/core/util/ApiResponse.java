package project.fin_the_pen.finClient.core.util;

import lombok.AllArgsConstructor;
import project.fin_the_pen.model.user.type.ApiStatus;

@AllArgsConstructor
public class ApiResponse {
    private ApiStatus apiStatus;
    private String message;
    private Object data;

    public static ApiResponse success(Object data) {
        return new ApiResponse(ApiStatus.SUCCESS, null, data);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(ApiStatus.ERROR, message, null);
    }
}
