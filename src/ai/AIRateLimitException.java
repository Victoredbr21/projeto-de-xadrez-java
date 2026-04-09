package ai;

/**
 * Lançada quando a API retorna HTTP 429 (Too Many Requests).
 * O loop principal deve aguardar e tentar novamente.
 */
public class AIRateLimitException extends AIException {

    private final long retryAfterMs;

    public AIRateLimitException(String providerName, long retryAfterMs) {
        super("[" + providerName + "] Rate limit atingido. Aguardando " + retryAfterMs + "ms antes de tentar novamente.", 429);
        this.retryAfterMs = retryAfterMs;
    }

    public long getRetryAfterMs() {
        return retryAfterMs;
    }
}
