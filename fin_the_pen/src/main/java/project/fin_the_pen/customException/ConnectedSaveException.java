package project.fin_the_pen.customException;

public class ConnectedSaveException extends RuntimeException {
    public ConnectedSaveException(String message) {
        super(message);
    }
}
