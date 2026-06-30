package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import funcionario.Funcionario;
import venda.Cliente;
import venda.Pagamento;

public class VendaService {

    public static void gerarNotaFiscal(Cliente cliente, List<MenuFuncionario.ItemCarrinho> carrinho, Pagamento pagamento, Funcionario vendedor) {
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String clienteLabel;
        if (cliente != null && cliente.getCpf() != null && !cliente.getCpf().equals("99999999999")) {
            clienteLabel = "Cliente: " + cliente.getNome() + " (CPF: " + formatarCpf(cliente.getCpf()) + ")";
        } else {
            clienteLabel = "Cliente não identificado (CPF: 999.999.999-99)";
        }

        System.out.println("\n==================================================");
        System.out.println("NOTA FISCAL");
        System.out.println("==================================================");
        System.out.println(clienteLabel);
        System.out.println("Data: " + dataAtual);
        System.out.println("Vendedor: " + vendedor.getNome() + " (Matrícula: " + vendedor.getMatricula() + ")");
        System.out.println();
        System.out.println("---");
        System.out.println("Item | Qtd | Unitário | Subtotal");
        System.out.println();
        System.out.println("---");

        double total = 0.0;
        for (MenuFuncionario.ItemCarrinho itemCarrinho : carrinho) {
            double subtotal = itemCarrinho.item.getPrecoVenda() * itemCarrinho.quantidade;
            total += subtotal;
            System.out.printf("%s | %d | R$ %.2f | R$ %.2f%n",
                itemCarrinho.item.getTitulo(),
                itemCarrinho.quantidade,
                itemCarrinho.item.getPrecoVenda(),
                subtotal);
        }

        System.out.println();
        System.out.println("---");
        System.out.printf("Total: R$ %.2f%n", total);
        System.out.println("Forma de pagamento: " + pagamento.getMetodoPagamento());
        System.out.println();
        System.out.println("---");
        System.out.println("Obrigado pela preferência!");
        System.out.println("==================================================");
    }

    public static String gerarCodigoVenda() {
        return "V" + System.currentTimeMillis();
    }

    public static String formatarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return "999.999.999-99";
        }

        String numeros = cpf.replaceAll("\\D", "");
        if (numeros.length() == 11) {
            return numeros.substring(0, 3) + "." + numeros.substring(3, 6) + "." + numeros.substring(6, 9) + "-" + numeros.substring(9);
        }

        return cpf;
    }
}
