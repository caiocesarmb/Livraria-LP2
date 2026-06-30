package main;

import funcionario.Estoquista;
import funcionario.Funcionario;
import funcionario.Gerente;
import funcionario.Vendedor;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuGerente {

    public static void menuGerente(Scanner scanner) {
        System.out.println("Login do Gerente:");
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine().trim();

        Funcionario gerenteLogado = null;
        for (Funcionario f : Main.funcionarios) {
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

    public static void cadastrarNovoFuncionario(Scanner scanner) {
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
            Funcionario novoFuncionario;
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

            Main.funcionarios.add(novoFuncionario);
            PersistenciaService.salvarDados();
            System.out.println("Funcionário cadastrado com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro ao processar entrada.");
        }
    }

    public static void demitirFuncionario(Scanner scanner) {
        System.out.println("\n--- Demitir Funcionário ---");
        System.out.print("Matrícula do funcionário: ");
        String matricula = scanner.nextLine().trim();

        for (Funcionario f : new ArrayList<>(Main.funcionarios)) {
            if (f.getMatricula().equals(matricula)) {
                if (f instanceof Gerente) {
                    long totalGerentes = Main.funcionarios.stream().filter(g -> g instanceof Gerente).count();
                    if (totalGerentes <= 1) {
                        System.out.println("❌ Não é possível demitir o único gerente.");
                        return;
                    }
                }

                Main.funcionarios.remove(f);
                PersistenciaService.salvarDados();
                System.out.println("Funcionário " + f.getNome() + " demitido com sucesso!");
                return;
            }
        }

        System.out.println("Funcionário não encontrado.");
    }

    public static void listarFuncionarios() {
        System.out.println("\n--- Lista de Funcionários ---");
        if (Main.funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        for (Funcionario f : Main.funcionarios) {
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

    public static void listarCargos() {
        System.out.println("\n--- Lista de Cargos ---");
        System.out.println("Gerente");
        System.out.println("Vendedor");
        System.out.println("Estoquista");
    }
}
