package application;

import chess.PecaXadrez;


public class UI {

    //metodos

    // aqui imprime o tabuleiro
    public static void imprimirTabuleiro(PecaXadrez[][] pecas){
    for (int i=0;i<pecas.length;i++){   // primeiro for para percorrer as pecas
        System.out.print((8 - i) + " ");
        for (int j=0;j<pecas.length;j++){  // segundo for apra percorrer a outra array
            imprimirUmaPeca(pecas[i][j]);
        }
        System.out.println();           // quebra de linha que eu fiz para o tabuleiro
      }
        System.out.println("  a b c d e f g h");
    }

public static void imprimirUmaPeca(PecaXadrez peca){

        if (peca == null) {      // null é o valor da casa vazia entao se uma peca for null ela vai mostrar como -
            System.out.println("-");
        } else {
            System.out.println(peca); // caso contrario ele imprime a peca
        }
    System.out.println(" ");
}

} //fecha classe
