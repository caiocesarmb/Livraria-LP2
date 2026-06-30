package main;

import item.estoque.Eletronico;
import item.estoque.ItemEstoque;
import item.estoque.Livro;
import item.estoque.Papelaria;

public class EstoqueService {

    public static void listaEstoque() {
        System.out.println("\n--- Lista do Estoque ---");
        if (Main.estoque.isEmpty()) {
            System.out.println("Estoque vazio.");
            return;
        }
        for (ItemEstoque item : Main.estoque) {
            System.out.printf(
                "Código: %s | Título: %s | Preço: R$ %.2f | Quantidade: %d%n",
                item.getCodigo(),
                item.getTitulo(),
                item.getPrecoVenda(),
                item.getQuantidadeAtual()
            );
        }
    }
}
