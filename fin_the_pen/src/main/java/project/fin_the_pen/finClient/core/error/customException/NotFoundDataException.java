package project.fin_the_pen.finClient.core.error.customException;

public class NotFoundDataException extends RuntimeException {
    public NotFoundDataException(String message) {
        super(message);
    }
}
