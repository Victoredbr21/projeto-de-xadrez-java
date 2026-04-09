package ai;

/**
 * Lançada quando a API retorna HTTP 400 (Bad Request).
 * Geralmente indica prompt malformado ou parâmetros inválidos.
 */
public class AIBadRequestException extends AIException {

    public AIBadRequestException(String providerName, String detail) {
        super("[" + providerName + "] Bad Request: " + detail, 400);
    }
}
