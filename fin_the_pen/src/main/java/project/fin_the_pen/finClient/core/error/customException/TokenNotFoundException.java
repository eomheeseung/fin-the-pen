package project.fin_the_pen.finClient.core.error.customException;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
