package project.fin_the_pen.finClient.core.error.customException;

public class DuplicatedScheduleException extends RuntimeException {
    public DuplicatedScheduleException(String message) {
        super(message);
    }
}
