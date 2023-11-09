package project.fin_the_pen.finClient.core.error.customException;

public class NotFoundScheduleException extends RuntimeException {
    public NotFoundScheduleException() {
        super();
    }

    public NotFoundScheduleException(String message) {
        super(message);
    }

    public NotFoundScheduleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundScheduleException(Throwable cause) {
        super(cause);
    }

    protected NotFoundScheduleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
