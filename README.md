# ♿️ Chess — Java + AI Battle

> Jogo de xadrez completo no terminal com suporte a partidas **Humano vs Humano**, **Humano vs IA** e o modo mais insano: **IA vs IA** — Groq (Llama 3.3) disputando contra Gemini (Flash 2.0) em tempo real.

![Java](https://img.shields.io/badge/Java-11%2B-orange?style=flat-square&logo=openjdk)
![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)
![Status](https://img.shields.io/badge/status-active-brightgreen?style=flat-square)

---

## 🎮 Modos de Jogo

| Modo | Descrição |
|---|---|
| 👥 Humano vs Humano | Dois jogadores no mesmo terminal |
| 🧠 Humano vs IA | Você escolhe jogar contra Groq ou Gemini |
| 🤖 IA vs IA | Groq (Branco) enfrenta Gemini (Preto) de forma autônoma |

Ao iniciar, um menu interativo pergunta o modo e as preferências:

```
╔══════════════════════════════════════╗
║        XADREZ - MODO DE JOGO         ║
╠══════════════════════════════════════╣
║  1. Humano vs Humano                 ║
║  2. Humano vs IA                     ║
║  3. IA vs IA  (Groq vs Gemini)       ║
╚══════════════════════════════════════╝
```

---

## 📺 Preview

```
8 t c b q r b c t 
7 p p p p p p p p 
6 - - - - - - - - 
5 - - - - - - - - 
4 - - - - P - - - 
3 - - - - - - - - 
2 P P P P - P P P 
1 T C B Q R B C T 
  a b c d e f g h

Turno : 3
[Gemini (Flash 2.0)] pensando...
[Gemini (Flash 2.0)] jogou: e7 → e5
```

---

## 🚀 Como executar

### Pré-requisitos

- Java JDK 11 ou superior
- Terminal com suporte a cores ANSI (Linux, macOS ou Windows Terminal)
- API keys da [Groq](https://console.groq.com/keys) e/ou [Google AI Studio](https://aistudio.google.com/app/apikey) *(apenas para modos com IA)*

### 1. Clone o repositório

```bash
git clone https://github.com/Victoredbr21/projeto-de-xadrez-java.git
cd projeto-de-xadrez-java
```

### 2. Configure as API keys

Copie o arquivo de exemplo e preencha com suas chaves:

```bash
cp .env.example .env
```

**.env**
```env
GROQ_API_KEY=gsk_seu_token_aqui
GEMINI_API_KEY=AIzaSy_seu_token_aqui
```

> ⚠️ O arquivo `.env` já está no `.gitignore` — suas chaves nunca vão para o repositório.

**No terminal (sessão atual):**
```bash
# Linux / macOS
export GROQ_API_KEY="gsk_seu_token"
export GEMINI_API_KEY="AIzaSy_seu_token"

# PowerShell
$env:GROQ_API_KEY = "gsk_seu_token"
$env:GEMINI_API_KEY = "AIzaSy_seu_token"
```

**Permanente no Windows:**
```powershell
[System.Environment]::SetEnvironmentVariable("GROQ_API_KEY", "gsk_seu_token", "User")
[System.Environment]::SetEnvironmentVariable("GEMINI_API_KEY", "AIzaSy_seu_token", "User")
```

### 3. Compile e execute

```bash
javac -d out -sourcepath src src/application/Programa.java
java -cp out application.Programa
```

**Ou pelo IntelliJ IDEA:**
- File → Open → seleciona a pasta do projeto
- Run → Edit Configurations → adicione as variáveis em *Environment variables*
- Clique direito em `Programa.java` → Run

---

## 🎮 Como jogar (modo humano)

1. Digite a **posição de origem** da peça (ex: `e2`)
2. As casas em **azul** mostram os movimentos possíveis
3. Digite a **posição de destino** (ex: `e4`)
4. O turno passa automaticamente ao outro jogador
5. Promoção de peão: escolha `Q` Raínha | `T` Torre | `B` Bispo | `C` Cavalo

> No modo IA vs IA é só sentar e assistir 🍿

---

## ✅ Funcionalidades

### Regras do xadrez
- [x] Tabuleiro 8x8 com coordenadas `a1` a `h8`
- [x] Movimentação correta de todas as 6 peças
- [x] Captura, validação de movimentos ilegais e alternância de turnos
- [x] Destaque visual dos movimentos possíveis (fundo azul)
- [x] **Xeque** — detectado e exibido em vermelho
- [x] **Xeque-mate** — encerra o jogo e declara o vencedor
- [x] **Roque pequeno e grande**
- [x] **En Passant**
- [x] **Promoção do Peão**

### Integração com IA
- [x] Interface `AIPlayer` — qualquer IA é intercambiável com humanos
- [x] **GroqPlayer** — modelo `llama-3.3-70b-versatile`, cooldown 1500ms
- [x] **GeminiPlayer** — modelo `gemini-2.0-flash`, cooldown 2000ms
- [x] **MoveParser** — extrai a jogada da resposta mesmo com texto extra (regex)
- [x] Retry automático em rate limit com leitura do header `Retry-After`
- [x] Promoção automática para Rainha no modo IA
- [x] Limite de 5 erros consecutivos antes de encerrar a partida

---

## 🚨 Tratamento de Erros de API

| Exceção | Gatilho | Comportamento |
|---|---|---|
| `AIAuthException` | HTTP 401/403 | Aborta imediatamente — chave inválida não tem retry |
| `AIRateLimitException` | HTTP 429 | Aguarda o tempo do header `Retry-After` e continua |
| `AIBadRequestException` | HTTP 400 | Conta como erro, tenta novamente até o limite |
| `AITimeoutException` | Timeout 30s | Conta como erro, tenta novamente até o limite |
| `AIException` | Generico/parse | Conta como erro, aguarda 1s e tenta novamente |
| `ChessException` | Jogada inválida | Conta como erro — IA gerou posição ilegal |

---

## 🗂️ Estrutura do Projeto

```
src/
├── application/
│   ├── Programa.java        # Entry point — inicializa menu e game loop
│   ├── MenuInicial.java     # Menu interativo de seleção de modo
│   ├── GameConfig.java      # Agrupa modo + jogadores + scanner
│   ├── GameLoop.java        # Loop unificado para todos os modos
│   ├── GameMode.java        # Enum: HUMANO_VS_HUMANO, HUMANO_VS_IA, IA_VS_IA
│   └── UI.java              # Interface terminal + getBoardAsString()
│
├── ai/
│   ├── AIPlayer.java        # Interface contrato de todo jogador
│   ├── HumanPlayer.java     # Jogador humano (implementa AIPlayer)
│   ├── GroqPlayer.java      # API Groq — llama-3.3-70b-versatile
│   ├── GeminiPlayer.java    # API Gemini — gemini-2.0-flash
│   ├── MoveParser.java      # Extrai jogada válida da resposta da IA
│   ├── AIException.java         # Base de todas as exceções de IA
│   ├── AIAuthException.java     # HTTP 401/403
│   ├── AIBadRequestException.java # HTTP 400
│   ├── AIRateLimitException.java  # HTTP 429
│   └── AITimeoutException.java    # Timeout de conexão
│
├── boardgame/               # Camada genérica de tabuleiro
│   ├── Tabuleiro.java
│   ├── Peca.java
│   └── Posicao.java
│
└── chess/                   # Regras específicas do xadrez
    ├── PartidaXadrez.java
    ├── PecaXadrez.java
    ├── PosicaoXadrez.java
    ├── Cor.java
    ├── ChessExection.java
    └── pecas/
        ├── Rei.java
        ├── Rainha.java
        ├── Torre.java
        ├── Bispo.java
        ├── Cavalo.java
        └── Peao.java
```

---

## 🏗️ Arquitetura

O projeto separa responsabilidades em três camadas independentes:

- **`boardgame`** — lógica genérica de tabuleiro, reaproveitável para qualquer jogo de matriz
- **`chess`** — regras específicas do xadrez sobre a camada genérica
- **`application`** — interface com usuário, menu e game loop
- **`ai`** — integração com APIs externas, completamente desacoplada do engine de xadrez

### Padrões utilizados
- **Interface + Polimorfismo** — `AIPlayer` permite trocar qualquer jogador sem alterar o loop
- **Herança** — cada peça sobrescreve `possiveisMovimentos()` com sua própria lógica
- **Hierarquia de exceções** — cada tipo de falha de API tem tratamento específico
- **Encapsulamento** — estado do jogo protegido dentro de `PartidaXadrez`

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 11+ | Linguagem principal |
| `java.net.http.HttpClient` | Chamadas HTTP nativas para as APIs de IA |
| Groq API | Modelo `llama-3.3-70b-versatile` como jogador |
| Google Gemini API | Modelo `gemini-2.0-flash` como jogador |
| Streams API | Filtragem de listas de peças |
| Códigos ANSI | Cores e destaque no terminal |
| Regex | Parser de jogadas das respostas das IAs |

---

## 👨‍💻 Autor

**Victor Eduardo Meireles**
- GitHub: [@Victoredbr21](https://github.com/Victoredbr21)
