package main;

import funcionario.Estoquista;
import funcionario.Funcionario;
import funcionario.Gerente;
import funcionario.Vendedor;
import item.estoque.Eletronico;
import item.estoque.ItemEstoque;
import item.estoque.Livro;
import item.estoque.Papelaria;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import venda.Cliente;

public class PersistenciaService {

    private static final Path BASE_DIR = Path.of(System.getProperty("user.dir"));
    private static final Path ARQUIVO_FUNCIONARIOS = BASE_DIR.resolve("funcionarios.txt");
    private static final Path ARQUIVO_ESTOQUE = BASE_DIR.resolve("estoque.txt");
    private static final Path ARQUIVO_CLIENTES = BASE_DIR.resolve("clientes.txt");

    public static void carregarDados() {
        try {
            Files.createDirectories(BASE_DIR);
        } catch (IOException e) {
            System.out.println("Erro ao preparar diretório de dados: " + e.getMessage());
        }

        Main.funcionarios.clear();
        Main.estoque.clear();
        Main.clientes.clear();

        if (Files.exists(ARQUIVO_FUNCIONARIOS) || Files.exists(ARQUIVO_ESTOQUE) || Files.exists(ARQUIVO_CLIENTES)) {
            carregarFuncionarios();
            carregarEstoque();
            carregarClientes();
            return;
        }

        Main.inicializarDadosPadrao();
        salvarDados();
    }

    public static void salvarDados() {
        salvarFuncionarios();
        salvarEstoque();
        salvarClientes();
    }

    public static void carregarFuncionarios() {
        if (!Files.exists(ARQUIVO_FUNCIONARIOS)) {
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_FUNCIONARIOS);
            for (String linha : linhas) {
                String[] partes = linha.split("\\|");
                if (partes.length < 4) {
                    continue;
                }

                String tipo = partes[0];
                String matricula = partes[1];
                String nome = partes[2];
                String cpf = partes[3];

                switch (tipo) {
                    case "GERENTE":
                        Main.funcionarios.add(new Gerente(matricula, nome, cpf));
                        break;
                    case "ESTOQUISTA":
                        Main.funcionarios.add(new Estoquista(matricula, nome, cpf, false));
                        break;
                    case "VENDEDOR":
                    default:
                        Main.funcionarios.add(new Vendedor(matricula, nome, cpf));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    public static void carregarEstoque() {
        if (!Files.exists(ARQUIVO_ESTOQUE)) {
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_ESTOQUE);
            for (String linha : linhas) {
                String[] partes = linha.split("\\|");
                if (partes.length < 7) {
                    continue;
                }

                String tipo = partes[0];
                ItemEstoque item;
                switch (tipo) {
                    case "LIVRO":
                        item = new Livro();
                        break;
                    case "ELETRONICO":
                        item = new Eletronico();
                        break;
                    case "PAPELARIA":
                    default:
                        item = new Papelaria();
                        break;
                }

                item.setCodigo(partes[1]);
                item.setTitulo(partes[2]);
                item.setPrecoCusto(Double.parseDouble(partes[3]));
                item.setPrecoVenda(Double.parseDouble(partes[4]));
                item.setQuantidadeAtual(Integer.parseInt(partes[5]));
                item.setQuantidadeMinima(Integer.parseInt(partes[6]));
                if (partes.length > 7 && !partes[7].isBlank()) {
                    item.setDataCadastro(LocalDate.parse(partes[7]));
                } else {
                    item.setDataCadastro(LocalDate.now());
                }
                Main.estoque.add(item);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar estoque: " + e.getMessage());
        }
    }

    public static void carregarClientes() {
        if (!Files.exists(ARQUIVO_CLIENTES)) {
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_CLIENTES);
            for (String linha : linhas) {
                String[] partes = linha.split("\\|");
                if (partes.length < 4) {
                    continue;
                }

                Main.clientes.add(new Cliente(partes[0], partes[1], partes[2], LocalDate.parse(partes[3])));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    public static void salvarFuncionarios() {
        try {
            List<String> linhas = new ArrayList<>();
            for (Funcionario funcionario : Main.funcionarios) {
                String tipo = funcionario instanceof Gerente ? "GERENTE"
                        : funcionario instanceof Estoquista ? "ESTOQUISTA"
                        : "VENDEDOR";
                linhas.add(String.join("|", tipo, funcionario.getMatricula(), funcionario.getNome(), funcionario.getCpf()));
            }
            Files.write(ARQUIVO_FUNCIONARIOS, linhas);
        } catch (IOException e) {
            System.out.println("Erro ao salvar funcionários: " + e.getMessage());
        }
    }

    public static void salvarEstoque() {
        try {
            List<String> linhas = new ArrayList<>();
            for (ItemEstoque item : Main.estoque) {
                String tipo = item instanceof Livro ? "LIVRO"
                        : item instanceof Eletronico ? "ELETRONICO"
                        : "PAPELARIA";
                linhas.add(String.join("|", tipo, item.getCodigo(), item.getTitulo(),
                        String.valueOf(item.getPrecoCusto()), String.valueOf(item.getPrecoVenda()),
                        String.valueOf(item.getQuantidadeAtual()), String.valueOf(item.getQuantidadeMinima()),
                        item.getDataCadastro() != null ? item.getDataCadastro().toString() : ""));
            }
            Files.write(ARQUIVO_ESTOQUE, linhas);
        } catch (IOException e) {
            System.out.println("Erro ao salvar estoque: " + e.getMessage());
        }
    }

    public static void salvarClientes() {
        try {
            List<String> linhas = new ArrayList<>();
            for (Cliente cliente : Main.clientes) {
                linhas.add(String.join("|", cliente.getCpf(), cliente.getNome(), cliente.getEmail(),
                        cliente.getDataCadastro() != null ? cliente.getDataCadastro().toString() : ""));
            }
            Files.write(ARQUIVO_CLIENTES, linhas);
        } catch (IOException e) {
            System.out.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }
}
