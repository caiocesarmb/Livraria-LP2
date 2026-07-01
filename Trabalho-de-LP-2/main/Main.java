package main;

import funcionario.Estoquista;
import funcionario.Funcionario;
import funcionario.Gerente;
import funcionario.Vendedor;
import item.estoque.Eletronico;
import item.estoque.ItemEstoque;
import item.estoque.Livro;
import item.estoque.Papelaria;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import venda.Cliente;
import venda.Venda;

public class Main {

    static List<Funcionario> funcionarios = new ArrayList<>();
    static List<ItemEstoque> estoque = new ArrayList<>();
    static List<Cliente> clientes = new ArrayList<>();
    private static List<Venda> vendasRealizadas = new ArrayList<>();

    public static List<Venda> getVendasRealizadas() {
        return vendasRealizadas;
    }

    public static void adicionarVendaRealizada(Venda venda) {
        if (venda != null) {
            vendasRealizadas.add(venda);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PersistenciaService.carregarDados();
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n-------------MENU----------");
            System.out.println("1-Gerente");
            System.out.println("2-Funcionario");
            System.out.println("3-Lista do estoque");
            System.out.println("0-Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            switch (opcao) {
                case 1:
                    MenuGerente.menuGerente(scanner);
                    break;
                case 2:
                    MenuFuncionario.menuFuncionario(scanner);
                    break;
                case 3:
                    EstoqueService.listaEstoque();
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    static void inicializarDadosPadrao() {
        Gerente gerente = new Gerente("G001", "João Silva", "12345678900");
        funcionarios.add(gerente);

        Vendedor vendedor1 = new Vendedor("V001", "Maria Santos", "98765432100");
        Vendedor vendedor2 = new Vendedor("V002", "Pedro Costa", "55555555500");
        funcionarios.add(vendedor1);
        funcionarios.add(vendedor2);

        Estoquista estoquista = new Estoquista("E001", "Carlos Oliveira", "11111111100", true);
        funcionarios.add(estoquista);

        Livro livro1 = new Livro();
        livro1.setCodigo("L001");
        livro1.setTitulo("Clean Code");
        livro1.setAutor("Robert C. Martin");
        livro1.setEditora("Prentice Hall");
        livro1.setIsbn("978-0132350884");
        livro1.setGenero("Programação");
        livro1.setEdicao(1);
        livro1.setPrecoCusto(50.0);
        livro1.setPrecoVenda(89.90);
        livro1.setQuantidadeAtual(5);
        livro1.setQuantidadeMinima(2);
        livro1.setDataCadastro(LocalDate.now());
        estoque.add(livro1);

        Livro livro2 = new Livro();
        livro2.setCodigo("L002");
        livro2.setTitulo("Design Patterns");
        livro2.setAutor("Gang of Four");
        livro2.setEditora("Addison-Wesley");
        livro2.setIsbn("978-0201633610");
        livro2.setGenero("Programação");
        livro2.setEdicao(1);
        livro2.setPrecoCusto(45.0);
        livro2.setPrecoVenda(79.90);
        livro2.setQuantidadeAtual(3);
        livro2.setQuantidadeMinima(1);
        livro2.setDataCadastro(LocalDate.now());
        estoque.add(livro2);

        Eletronico eletronico1 = new Eletronico(12, "SERIAL123456");
        eletronico1.setCodigo("E001");
        eletronico1.setTitulo("Mouse Logitech MX Master");
        eletronico1.setPrecoCusto(80.0);
        eletronico1.setPrecoVenda(249.90);
        eletronico1.setQuantidadeAtual(8);
        eletronico1.setQuantidadeMinima(3);
        eletronico1.setDataCadastro(LocalDate.now());
        estoque.add(eletronico1);

        Papelaria papelaria1 = new Papelaria();
        papelaria1.setCodigo("P001");
        papelaria1.setTitulo("Caneta Azul Pilot");
        papelaria1.setMarca("Pilot");
        papelaria1.setMaterialEscolar(true);
        papelaria1.setPrecoCusto(1.0);
        papelaria1.setPrecoVenda(2.50);
        papelaria1.setQuantidadeAtual(100);
        papelaria1.setQuantidadeMinima(20);
        papelaria1.setDataCadastro(LocalDate.now());
        estoque.add(papelaria1);

        Cliente cliente1 = new Cliente("12345678900", "Ana Silva", "ana@email.com", LocalDate.now());
        clientes.add(cliente1);
    }
}
