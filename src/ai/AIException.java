package ai;

/**
 * Exceção base para erros relacionados às chamadas de API das IAs.
 */
public class AIException extends Exception {

    private final int statusCode;

    public AIException(String message) {
        super(message);
        this.statusCode = -1;
    }

    public AIException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AIException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "AIException[status=" + statusCode + "]: " + getMessage();
    }
}
