package ai;

/**
 * Lançada quando a API retorna HTTP 401 ou 403.
 * Indica chave de API inválida ou sem permissão.
 */
public class AIAuthException extends AIException {

    public AIAuthException(String providerName) {
        super("[" + providerName + "] Autenticação falhou. Verifique a API key (variável de ambiente).", 401);
    }
}
