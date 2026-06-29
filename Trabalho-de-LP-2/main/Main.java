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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import venda.Cliente;
import venda.Pagamento;
import venda.PagamentoDinheiro;
import venda.PagamentoPix;
import venda.Venda;
import venda.VendaRepository;

public class Main {

    private static List<Funcionario> funcionarios = new ArrayList<>();
    private static List<ItemEstoque> estoque = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarDados();
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
                    menuGerente(scanner);
                    break;
                case 2:
                    menuFuncionario(scanner);
                    break;
                case 3:
                    listaEstoque();
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

    private static void inicializarDados() {
        // Adicionar gerente padrão
        Gerente gerente = new Gerente("G001", "João Silva", "12345678900");
        funcionarios.add(gerente);

        // Adicionar alguns vendedores
        Vendedor vendedor1 = new Vendedor("V001", "Maria Santos", "98765432100");
        Vendedor vendedor2 = new Vendedor("V002", "Pedro Costa", "55555555500");
        funcionarios.add(vendedor1);
        funcionarios.add(vendedor2);

        // Adicionar estoquista
        Estoquista estoquista = new Estoquista("E001", "Carlos Oliveira", "11111111100", true);
        funcionarios.add(estoquista);

        // Adicionar itens de estoque
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

        // Adicionar clientes
        Cliente cliente1 = new Cliente("12345678900", "Ana Silva", "ana@email.com", LocalDate.now());
        clientes.add(cliente1);
    }

    private static void menuGerente(Scanner scanner) {
        System.out.println("Login do Gerente:");
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine().trim();

        // Validar se é realmente gerente
        Funcionario gerenteLogado = null;
        for (Funcionario f : funcionarios) {
            if (f.getMatricula().equals(matricula) && f instanceof Gerente) {
                gerenteLogado = f;
                break;
            }
        }

        if (gerenteLogado == null) {
            System.out.println("Gerente não encontrado ou matrícula inválida.");
            return;
        }

        System.out.println("\nBem-vindo, " + gerenteLogado.getNome());
        System.out.println("---------MENU DO GERENTE-----------");
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n1-Cadastrar novo funcionário");
            System.out.println("2-Demitir funcionário");
            System.out.println("3-Listar funcionários");
            System.out.println("4-Listar cargos");
            System.out.println("0-Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1:
                    cadastrarNovoFuncionario(scanner);
                    break;
                case 2:
                    demitirFuncionario(scanner);
                    break;
                case 3:
                    listarFuncionarios();
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 4:
                    listarCargos();
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarNovoFuncionario(Scanner scanner) {
        System.out.println("\n--- Cadastro de Novo Funcionário ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine().trim();

        System.out.println("Tipo de funcionário:");
        System.out.println("1-Vendedor");
        System.out.println("2-Estoquista");
        System.out.println("3-Gerente");
        System.out.print("Escolha: ");

        try {
            int tipo = Integer.parseInt(scanner.nextLine().trim());

            Funcionario novoFuncionario = null;
            switch (tipo) {
                case 1:
                    novoFuncionario = new Vendedor(matricula, nome, cpf);
                    break;
                case 2:
                    System.out.print("Habilitado para empilhadeira? (S/N): ");
                    boolean habilitado = scanner.nextLine().trim().equalsIgnoreCase("S");
                    novoFuncionario = new Estoquista(matricula, nome, cpf, habilitado);
                    break;
                case 3:
                    novoFuncionario = new Gerente(matricula, nome, cpf);
                    break;
                default:
                    System.out.println("Tipo inválido.");
                    return;
            }

            funcionarios.add(novoFuncionario);
            System.out.println("Funcionário cadastrado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Erro ao processar entrada.");
        }
    }

    private static void demitirFuncionario(Scanner scanner) {
        System.out.println("\n--- Demitir Funcionário ---");
        System.out.print("Matrícula do funcionário: ");
        String matricula = scanner.nextLine().trim();

        for (Funcionario f : funcionarios) {
            if (f.getMatricula().equals(matricula)) {
                funcionarios.remove(f);
                System.out.println("Funcionário " + f.getNome() + " demitido com sucesso!");
                return;
            }
        }

        System.out.println("Funcionário não encontrado.");
    }

    private static void listarFuncionarios() {
        System.out.println("\n--- Lista de Funcionários ---");
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        for (Funcionario f : funcionarios) {
            String tipo = f.getClass().getSimpleName();
            System.out.printf("Matrícula: %s | Nome: %s | CPF: %s | Tipo: %s | Ativo: %s%n",
                f.getMatricula(),
                f.getNome(),
                f.getCpf(),
                tipo,
                f.isAtivo()
            );
        }
    }

    private static void listarCargos() {
        System.out.println("\n--- Lista de Cargos ---");
        System.out.println("Gerente");
        System.out.println("Vendedor");
        System.out.println("Estoquista");
    }

    private static void menuFuncionario(Scanner scanner) {
        System.out.println("Login/Registro do Funcionário:");
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine().trim();

        Funcionario funcionarioLogado = null;
        for (Funcionario f : funcionarios) {
            if (f.getMatricula().equals(matricula)) {
                funcionarioLogado = f;
                break;
            }
        }

        if (funcionarioLogado == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.println("\nBem-vindo, " + funcionarioLogado.getNome());

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n------MENU FUNCIONÁRIO------");
            System.out.println("1-Venda");
            System.out.println("2-Estoque");
            System.out.println("3-Cadastro de Cliente");
            System.out.println("0-Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1:
                    menuVenda(scanner, funcionarioLogado);
                    break;
                case 2:
                    menuEstoque(scanner, funcionarioLogado);
                    break;
                case 3:
                    cadastrarCliente(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuVenda(Scanner scanner, Funcionario funcionario) {
        System.out.println("\n--- MENU DE VENDAS ---");
        System.out.print("CPF do cliente (deixe em branco para não identificado): ");
        String cpfCliente = scanner.nextLine().trim();

        Cliente cliente = null;
        if (cpfCliente.isEmpty()) {
            cliente = new Cliente("99999999999", "Cliente não identificado", "anonimo@email.com", LocalDate.now());
            clientes.add(cliente);
            System.out.println("Cliente não identificado. Prosseguindo com a venda.");
        } else {
            for (Cliente c : clientes) {
                if (c.getCpf().equals(cpfCliente)) {
                    cliente = c;
                    break;
                }
            }

            if (cliente == null) {
                System.out.println("Cliente não encontrado. Realize o cadastro antes de vender.");
                return;
            }
            System.out.println("Cliente encontrado: " + cliente.getNome());
        }

        List<ItemCarrinho> carrinho = new ArrayList<>();

        while (true) {
            System.out.print("Código do produto: ");
            String codigoProduto = scanner.nextLine().trim();

            ItemEstoque produto = null;
            for (ItemEstoque item : estoque) {
                if (item.getCodigo().equals(codigoProduto)) {
                    produto = item;
                    break;
                }
            }

            if (produto == null) {
                System.out.println("Produto não encontrado.");
            } else {
                System.out.println("Produto: " + produto.getTitulo() + " - R$ " + produto.getPrecoVenda());
                System.out.print("Quantidade: ");

                try {
                    int quantidade = Integer.parseInt(scanner.nextLine().trim());

                    if (quantidade <= 0) {
                        System.out.println("Quantidade inválida.");
                    } else if (quantidade > produto.getQuantidadeAtual()) {
                        System.out.println("Quantidade indisponível no estoque.");
                    } else {
                        boolean itemJaNoCarrinho = false;
                        for (ItemCarrinho itemCarrinho : carrinho) {
                            if (itemCarrinho.item.getCodigo().equals(produto.getCodigo())) {
                                itemCarrinho.quantidade += quantidade;
                                itemJaNoCarrinho = true;
                                break;
                            }
                        }

                        if (!itemJaNoCarrinho) {
                            carrinho.add(new ItemCarrinho(produto, quantidade));
                        }
                        System.out.println("Item adicionado ao carrinho.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao processar entrada.");
                }
            }

            System.out.print("Deseja adicionar mais itens? (S/N): ");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (!resposta.equals("S")) {
                break;
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("Carrinho vazio. Venda cancelada.");
            return;
        }

        System.out.println("\n--- RESUMO DO CARRINHO ---");
        double totalGeral = 0.0;
        for (ItemCarrinho itemCarrinho : carrinho) {
            double subtotal = itemCarrinho.item.getPrecoVenda() * itemCarrinho.quantidade;
            totalGeral += subtotal;
            System.out.printf(
                "%s | Qtd: %d | Subtotal: R$ %.2f%n",
                itemCarrinho.item.getTitulo(),
                itemCarrinho.quantidade,
                subtotal
            );
        }
        System.out.printf("Total geral: R$ %.2f%n", totalGeral);

        System.out.println("\n--- Formas de Pagamento ---");
        System.out.println("1-Dinheiro");
        System.out.println("2-PIX");
        System.out.print("Escolha: ");

        Pagamento pagamento = null;
        try {
            int tipoPagamento = Integer.parseInt(scanner.nextLine().trim());
            switch (tipoPagamento) {
                case 1:
                    pagamento = new PagamentoDinheiro();
                    pagamento.processar(totalGeral);
                    break;
                case 2:
                    pagamento = new PagamentoPix();
                    pagamento.processar(totalGeral);
                    break;
                default:
                    System.out.println("Tipo de pagamento inválido.");
                    return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro ao processar entrada.");
            return;
        }

        System.out.print("Confirmar venda? (S/N): ");
        String confirmar = scanner.nextLine().trim().toUpperCase();
        if (!confirmar.equals("S")) {
            System.out.println("Venda cancelada.");
            return;
        }

        for (ItemCarrinho itemCarrinho : carrinho) {
            itemCarrinho.item.setQuantidadeAtual(itemCarrinho.item.getQuantidadeAtual() - itemCarrinho.quantidade);
        }

        Venda venda = new Venda(gerarCodigoVenda(), cliente, funcionario);
        for (ItemCarrinho itemCarrinho : carrinho) {
            venda.adicionarItem(itemCarrinho.item, itemCarrinho.quantidade);
        }
        venda.setPagamento(pagamento);
        venda.finalizar();
        VendaRepository.salvarVenda(venda);

        gerarNotaFiscal(cliente, carrinho, pagamento, funcionario);
        System.out.println("\nVenda realizada com sucesso!");
    }

    private static void gerarNotaFiscal(Cliente cliente, List<ItemCarrinho> carrinho, Pagamento pagamento, Funcionario vendedor) {
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
        for (ItemCarrinho itemCarrinho : carrinho) {
            double subtotal = itemCarrinho.item.getPrecoVenda() * itemCarrinho.quantidade;
            total += subtotal;
            System.out.printf(
                "%s | %d | R$ %.2f | R$ %.2f%n",
                itemCarrinho.item.getTitulo(),
                itemCarrinho.quantidade,
                itemCarrinho.item.getPrecoVenda(),
                subtotal
            );
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

    private static String gerarCodigoVenda() {
        return "V" + System.currentTimeMillis();
    }

    private static String formatarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return "999.999.999-99";
        }

        String numeros = cpf.replaceAll("\\D", "");
        if (numeros.length() == 11) {
            return numeros.substring(0, 3) + "." + numeros.substring(3, 6) + "." + numeros.substring(6, 9) + "-" + numeros.substring(9);
        }

        return cpf;
    }

    private static class ItemCarrinho {
        private final ItemEstoque item;
        private int quantidade;

        private ItemCarrinho(ItemEstoque item, int quantidade) {
            this.item = item;
            this.quantidade = quantidade;
        }
    }

    private static void menuEstoque(Scanner scanner, Funcionario funcionario) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE ESTOQUE ---");
            System.out.println("1-Listar estoque");
            System.out.println("2-Consultar item específico");
            System.out.println("0-Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Erro ao processar entrada.");
                continue;
            }

            switch (opcao) {
                case 1:
                    listaEstoque();
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Código do item: ");
                    String codigo = scanner.nextLine().trim();
                    consultarItemEstoque(codigo);
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Voltando ao menu do funcionário...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void consultarItemEstoque(String codigo) {
        for (ItemEstoque item : estoque) {
            if (item.getCodigo().equals(codigo)) {
                System.out.println("\n--- Detalhes do Item ---");
                System.out.printf("Código: %s%n", item.getCodigo());
                System.out.printf("Título: %s%n", item.getTitulo());
                System.out.printf("Preço de Venda: R$ %.2f%n", item.getPrecoVenda());
                System.out.printf("Quantidade Atual: %d%n", item.getQuantidadeAtual());
                System.out.printf("Quantidade Mínima: %d%n", item.getQuantidadeMinima());
                System.out.printf("Categoria: %s%n", item.getCategoria());

                if (item instanceof Livro) {
                    Livro livro = (Livro) item;
                    System.out.printf("Autor: %s%n", livro.getAutor());
                    System.out.printf("Editora: %s%n", livro.getEditora());
                    System.out.printf("ISBN: %s%n", livro.getIsbn());
                } else if (item instanceof Eletronico) {
                    Eletronico eletronico = (Eletronico) item;
                    System.out.printf("Garantia (meses): %d%n", eletronico.getGarantiaMeses());
                } else if (item instanceof Papelaria) {
                    Papelaria papelaria = (Papelaria) item;
                    System.out.printf("Marca: %s%n", papelaria.getMarca());
                }

                return;
            }
        }
        System.out.println("Item não encontrado.");
    }

    private static void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- Cadastro de Cliente ---");
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();

        // Verificar se cliente já existe
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpf)) {
                System.out.println("Cliente já cadastrado.");
                return;
            }
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        Cliente novoCliente = new Cliente(cpf, nome, email, LocalDate.now());
        clientes.add(novoCliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void listaEstoque() {
        System.out.println("\n--- Lista do Estoque ---");
        if (estoque.isEmpty()) {
            System.out.println("Estoque vazio.");
            return;
        }
        for (ItemEstoque item : estoque) {
            System.out.printf(
                "Código: %s | Título: %s | Preço: R$ %.2f | Quantidade: %d%n",
                item.getCodigo(),
                item.getTitulo(),
                item.getPrecoVenda(),
                item.getQuantidadeAtual()
            );
        }
    }

    private static Vendedor gerarVendedorAutomatico() {
        String[] nomes = {
            "Carlos Souza",
            "Marina Alves",
            "Pedro Lima",
            "Fernanda Costa",
            "João Pereira",
        };
        Random random = new Random();
        String nome = nomes[random.nextInt(nomes.length)];
        String matricula = "V" + (1000 + random.nextInt(9000));
        String cpf = String.format("%011d", random.nextInt(1_000_000_000));
        return new Vendedor(matricula, nome, cpf);
    }
}
