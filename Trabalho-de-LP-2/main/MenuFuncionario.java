package main;

import exceptions.ClienteNaoEncontradoException;
import exceptions.EstoqueInsuficienteException;
import funcionario.Estoquista;
import funcionario.Funcionario;
import funcionario.Gerente;
import item.estoque.Eletronico;
import item.estoque.ItemEstoque;
import item.estoque.Livro;
import item.estoque.Papelaria;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import venda.Cliente;
import venda.Pagamento;
import venda.PagamentoDinheiro;
import venda.PagamentoPix;
import venda.Venda;
import venda.VendaRepository;
import permissao.Permissao;

public class MenuFuncionario {

    public static void menuFuncionario(Scanner scanner) {
        System.out.println("Login/Registro do Funcionário:");
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine().trim();

        Funcionario funcionarioLogado = null;
        for (Funcionario f : Main.funcionarios) {
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
            System.out.println("4-Devolução");
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
                case 4:
                    menuDevolucao(scanner, funcionarioLogado);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static void menuVenda(Scanner scanner, Funcionario funcionario) {
        System.out.println("\n--- MENU DE VENDAS ---");
        System.out.print("CPF do cliente (deixe em branco para não identificado): ");
        String cpfCliente = scanner.nextLine().trim();

        Cliente cliente = null;
        try {
            if (cpfCliente.isEmpty()) {
                cliente = new Cliente("99999999999", "Cliente não identificado", "anonimo@email.com", LocalDate.now());
                Main.clientes.add(cliente);
                System.out.println("Cliente não identificado. Prosseguindo com a venda.");
            } else {
                for (Cliente c : Main.clientes) {
                    if (c.getCpf().equals(cpfCliente)) {
                        cliente = c;
                        break;
                    }
                }

                if (cliente == null) {
                    throw new ClienteNaoEncontradoException("Cliente não encontrado. Realize o cadastro antes de vender.");
                }
                System.out.println("Cliente encontrado: " + cliente.getNome());
            }
        } catch (ClienteNaoEncontradoException e) {
            System.out.println(e.getMessage());
            return;
        }

        List<ItemCarrinho> carrinho = new ArrayList<>();
        while (true) {
            System.out.print("Código do produto: ");
            String codigoProduto = scanner.nextLine().trim();

            ItemEstoque produto = null;
            for (ItemEstoque item : Main.estoque) {
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
                        throw new EstoqueInsuficienteException("Quantidade indisponível no estoque.");
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
                } catch (EstoqueInsuficienteException e) {
                    System.out.println(e.getMessage());
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
            System.out.printf("%s | Qtd: %d | Subtotal: R$ %.2f%n",
                itemCarrinho.item.getTitulo(),
                itemCarrinho.quantidade,
                subtotal);
        }
        System.out.printf("Total geral: R$ %.2f%n", totalGeral);

        Venda venda = new Venda(VendaService.gerarCodigoVenda(), cliente, funcionario);
        for (ItemCarrinho itemCarrinho : carrinho) {
            venda.adicionarItem(itemCarrinho.item, itemCarrinho.quantidade);
        }

        if (funcionario.getPermissoes().contains(Permissao.APLICAR_DESCONTO_PADRAO)) {
            System.out.print("Deseja aplicar desconto? (S/N): ");
            String aplicarDesconto = scanner.nextLine().trim().toUpperCase();
            if (aplicarDesconto.equals("S")) {
                System.out.print("Valor do desconto (R$): ");
                try {
                    double desconto = Double.parseDouble(scanner.nextLine().trim());
                    venda.aplicarDesconto(desconto);
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido. Desconto não aplicado.");
                }
                System.out.println("\n--- RESUMO DO CARRINHO COM DESCONTO ---");
                for (ItemCarrinho itemCarrinho : carrinho) {
                    double subtotal = itemCarrinho.item.getPrecoVenda() * itemCarrinho.quantidade;
                    System.out.printf("%s | Qtd: %d | Subtotal: R$ %.2f%n",
                        itemCarrinho.item.getTitulo(),
                        itemCarrinho.quantidade,
                        subtotal);
                }
                System.out.printf("Desconto aplicado: R$ %.2f%n", venda.getDescontoAplicado());
                System.out.printf("Total final: R$ %.2f%n", venda.calcularTotal());
            }
        }

        double totalAPagar = venda.calcularTotal();

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
                    pagamento.processar(totalAPagar);
                    break;
                case 2:
                    pagamento = new PagamentoPix();
                    pagamento.processar(totalAPagar);
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

        venda.setPagamento(pagamento);
        try {
            venda.finalizar();
            VendaRepository.salvarVenda(venda);
            Main.adicionarVendaRealizada(venda);
            PersistenciaService.salvarDados();
        } catch (Exception e) {
            System.out.println("Erro ao finalizar venda: " + e.getMessage());
            return;
        }

        VendaService.gerarNotaFiscal(cliente, carrinho, pagamento, funcionario);
        System.out.println("\nVenda realizada com sucesso!");
    }

    public static void menuDevolucao(Scanner scanner, Funcionario funcionario) {
        System.out.println("\n--- MENU DE DEVOLUÇÃO ---");
        System.out.println("--- VENDAS FINALIZADAS ---");

        List<Venda> vendasFinalizadas = new ArrayList<>();
        for (Venda venda : Main.getVendasRealizadas()) {
            if (venda.getStatus() == enums.StatusVenda.FINALIZADA) {
                vendasFinalizadas.add(venda);
            }
        }

        if (vendasFinalizadas.isEmpty()) {
            System.out.println("Nenhuma venda finalizada encontrada.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Venda venda : vendasFinalizadas) {
                String nomeCliente = venda.getCliente() != null ? venda.getCliente().getNome() : "Cliente não identificado";
                String dataFormatada = venda.getDataVenda().format(formatter);
                String totalFormatado = String.format("R$ %.2f", venda.calcularTotal());
                System.out.printf("Código: %s | Cliente: %s | Data: %s | Total: %s | Status: %s%n",
                    venda.getCodigoVenda(),
                    nomeCliente,
                    dataFormatada,
                    totalFormatado,
                    venda.getStatus());
            }
        }

        System.out.print("Código da venda: ");
        String codigoVenda = scanner.nextLine().trim();

        Venda vendaEncontrada = null;
        for (Venda v : Main.getVendasRealizadas()) {
            if (v.getCodigoVenda().equals(codigoVenda)) {
                vendaEncontrada = v;
                break;
            }
        }

        if (vendaEncontrada == null) {
            System.out.println("Venda não encontrada.");
            return;
        }

        if (vendaEncontrada.getStatus() != enums.StatusVenda.FINALIZADA) {
            System.out.println("Apenas vendas finalizadas podem ser devolvidas.");
            return;
        }

        System.out.print("Confirmar devolução? (S/N): ");
        String confirmarDevolucao = scanner.nextLine().trim().toUpperCase();
        if (!confirmarDevolucao.equals("S")) {
            System.out.println("Devolução cancelada.");
            return;
        }

        try {
            vendaEncontrada.devolverTotal();

            for (Map.Entry<ItemEstoque, Integer> entrada : vendaEncontrada.getItensVenda().entrySet()) {
                ItemEstoque item = entrada.getKey();
                int quantidade = entrada.getValue();
                item.setQuantidadeAtual(item.getQuantidadeAtual() + quantidade);
            }

            PersistenciaService.salvarDados();
            System.out.println("Devolução realizada com sucesso.");
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        } catch (exceptions.VendaInvalidaException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void menuEstoque(Scanner scanner, Funcionario funcionario) {
        int opcao = -1;
        boolean podeGerenciar = funcionario instanceof Estoquista || funcionario instanceof Gerente;

        while (opcao != 0) {
            System.out.println("\n--- MENU DE ESTOQUE ---");
            System.out.println("1-Listar estoque");
            System.out.println("2-Consultar item específico");
            if (podeGerenciar) {
                System.out.println("3-Adicionar item ao estoque");
                System.out.println("4-Remover item do estoque");
                System.out.println("5-Atualizar quantidade do item");
            }
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
                    EstoqueService.listaEstoque();
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
                case 3:
                    if (podeGerenciar) {
                        adicionarItemEstoque(scanner);
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;
                case 4:
                    if (podeGerenciar) {
                        removerItemEstoque(scanner);
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;
                case 5:
                    if (podeGerenciar) {
                        atualizarQuantidadeEstoque(scanner);
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;
                case 0:
                    System.out.println("Voltando ao menu do funcionário...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static void consultarItemEstoque(String codigo) {
        for (ItemEstoque item : Main.estoque) {
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

    public static void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- Cadastro de Cliente ---");
        System.out.print("CPF: ");
        String cpf = scanner.nextLine().trim();

        for (Cliente c : Main.clientes) {
            if (c.getCpf().equals(cpf)) {
                System.out.println("Cliente já cadastrado.");
                return;
            }
        }

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        try {
            Cliente novoCliente = new Cliente(cpf, nome, email, LocalDate.now());
            Main.clientes.add(novoCliente);
            PersistenciaService.salvarDados();
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adicionarItemEstoque(Scanner scanner) {
        System.out.println("\n--- Adicionar Item ao Estoque ---");
        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Título: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("Preço de venda: ");
        double precoVenda;
        try {
            precoVenda = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido. Operação cancelada.");
            return;
        }
        System.out.print("Quantidade inicial: ");
        int quantidade;
        try {
            quantidade = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida. Operação cancelada.");
            return;
        }

        Livro novo = new Livro();
        novo.setCodigo(codigo);
        novo.setTitulo(titulo);
        novo.setPrecoCusto(0.0);
        novo.setPrecoVenda(precoVenda);
        novo.setQuantidadeAtual(quantidade);
        novo.setQuantidadeMinima(0);
        novo.setDataCadastro(LocalDate.now());
        Main.estoque.add(novo);
        PersistenciaService.salvarDados();
        System.out.println("Item adicionado com sucesso.");
    }

    private static void removerItemEstoque(Scanner scanner) {
        System.out.println("\n--- Remover Item do Estoque ---");
        System.out.print("Código do item a remover: ");
        String codigo = scanner.nextLine().trim();
        for (ItemEstoque item : new ArrayList<>(Main.estoque)) {
            if (item.getCodigo().equals(codigo)) {
                Main.estoque.remove(item);
                PersistenciaService.salvarDados();
                System.out.println("Item removido com sucesso.");
                return;
            }
        }
        System.out.println("Item não encontrado.");
    }

    private static void atualizarQuantidadeEstoque(Scanner scanner) {
        System.out.println("\n--- Atualizar Quantidade do Estoque ---");
        System.out.print("Código do item: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Nova quantidade: ");
        int novaQtd;
        try {
            novaQtd = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Quantidade inválida.");
            return;
        }
        for (ItemEstoque item : Main.estoque) {
            if (item.getCodigo().equals(codigo)) {
                try {
                    item.setQuantidadeAtual(novaQtd);
                    PersistenciaService.salvarDados();
                    System.out.println("Quantidade atualizada com sucesso.");
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
                return;
            }
        }
        System.out.println("Item não encontrado.");
    }

    static class ItemCarrinho {
        final ItemEstoque item;
        int quantidade;

        ItemCarrinho(ItemEstoque item, int quantidade) {
            this.item = item;
            this.quantidade = quantidade;
        }
    }
}
