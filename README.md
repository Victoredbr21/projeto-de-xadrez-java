# ♟️ Projeto de Xadrez em Java

Jogo de xadrez completo rodando no terminal, desenvolvido em Java com orientação a objetos. O projeto implementa todas as regras oficiais do xadrez, incluindo movimentos especiais, detecção de xeque e xeque-mate.

---

## 📸 Preview

```
8 t c b q r b c t
7 p p p p p p p p
6 - - - - - - - -
5 - - - - - - - -
4 - - - - - - - -
3 - - - - - - - -
2 P P P P P P P P
1 T C B Q R B C T
  a b c d e f g h

Turno : 1
Esperando o jogador: BRANCO
```

---

## 🚀 Como executar

### Pré-requisitos

- Java JDK 11 ou superior
- Terminal com suporte a cores ANSI (Linux, macOS ou Windows Terminal)

### Compilando e rodando

```bash
# Clone o repositório
git clone https://github.com/Victoredbr21/projeto-de-xadrez-java.git
cd projeto-de-xadrez-java

# Compile todos os arquivos
javac -d out -sourcepath src src/application/Programa.java

# Execute
java -cp out application.Programa
```

---

## 🎮 Como jogar

1. Digite a **posição de origem** da peça que deseja mover (ex: `e2`)
2. As casas em **azul** indicam os movimentos possíveis
3. Digite a **posição de destino** (ex: `e4`)
4. O turno passa automaticamente para o outro jogador
5. Caso um **Peão seja promovido**, o jogo pedirá a escolha da nova peça:
   - `Q` → Rainha
   - `T` → Torre
   - `B` → Bispo
   - `C` → Cavalo

---

## ✅ Funcionalidades implementadas

### Regras básicas
- [x] Tabuleiro 8x8 com coordenadas no estilo xadrez (`a1` a `h8`)
- [x] Movimentação de todas as 6 peças com regras corretas
- [x] Captura de peças adversárias
- [x] Validação de movimentos ilegais
- [x] Alternância de turnos (Branco → Preto → Branco...)
- [x] Destaque visual dos movimentos possíveis (fundo azul)
- [x] Exibição das peças capturadas

### Regras avançadas
- [x] **Xeque** — detecta e exibe `*** EM XEQUE! ***` em vermelho
- [x] **Xeque-mate** — encerra o jogo e declara o vencedor
- [x] **Proteção contra auto-xeque** — impede movimentos que deixem o próprio Rei em xeque
- [x] **Roque pequeno** — Rei move 2 casas para a direita (lado do Rei)
- [x] **Roque grande** — Rei move 2 casas para a esquerda (lado da Rainha)
- [x] **En Passant** — captura especial do Peão, válida apenas no turno seguinte ao avanço duplo
- [x] **Promoção do Peão** — ao atingir a última fileira, pede input ao jogador

---

## 🗂️ Estrutura do projeto

```
src/
├── application/
│   ├── Programa.java        # Ponto de entrada — loop principal do jogo
│   └── UI.java              # Interface do terminal (cores ANSI, tabuleiro, input)
│
├── boardgame/               # Camada genérica — independente do xadrez
│   ├── Tabuleiro.java       # Matriz de peças + validações de posição
│   ├── Peca.java            # Classe abstrata base de toda peça
│   └── Posicao.java         # Coordenada interna (linha/coluna 0-indexed)
│
└── chess/                   # Camada do xadrez
    ├── PartidaXadrez.java   # Motor do jogo: turnos, xeque, movimentos especiais
    ├── PecaXadrez.java      # Abstração das peças de xadrez (cor, posição)
    ├── PosicaoXadrez.java   # Coordenada no estilo xadrez (ex: e4)
    ├── Cor.java             # Enum BRANCO / PRETO
    ├── ChessExection.java   # Exceção customizada do domínio
    └── pecas/
        ├── Rei.java         # Rei — 8 direções + roque (flag primeirMovimento)
        ├── Rainha.java      # Rainha — combina Torre + Bispo
        ├── Torre.java       # Torre — linhas e colunas + flag primeirMovimento
        ├── Bispo.java       # Bispo — diagonais
        ├── Cavalo.java      # Cavalo — movimento em L
        └── Peao.java        # Peão — avanço simples/duplo, captura diagonal, en passant, promoção
```

---

## 🏗️ Arquitetura

O projeto aplica uma separação clara entre camadas:

- **`boardgame`** — lógica genérica de tabuleiro, sem qualquer conhecimento de xadrez. Poderia ser reaproveitada para damas ou outros jogos de tabuleiro.
- **`chess`** — implementa as regras específicas do xadrez sobre a camada genérica.
- **`application`** — responsável exclusivamente pela interface com o usuário no terminal.

### Padrões utilizados
- **Herança e polimorfismo** — cada peça sobrescreve `possiveisMovimentos()` com sua própria lógica
- **Encapsulamento** — estado do jogo protegido dentro de `PartidaXadrez`
- **Tratamento de exceções** — `ChessExection` para erros de domínio, capturada no loop principal sem derrubar o jogo

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 11+ | Linguagem principal |
| Streams API | Filtragem de listas de peças |
| Códigos ANSI | Cores e destaque no terminal |
| OOP | Arquitetura em camadas com herança e polimorfismo |

---

## 👨‍💻 Autor

**Victor Eduardo Meireles**
- GitHub: [@Victoredbr21](https://github.com/Victoredbr21)
