package ai;

/**
 * Lançada quando a requisição HTTP excede o timeout configurado.
 */
public class AITimeoutException extends AIException {

    public AITimeoutException(String providerName) {
        super("[" + providerName + "] Timeout na requisição. A API não respondeu a tempo.");
    }
}
