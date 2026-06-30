package venda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private String cpf;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private List<Venda> historicoCompras;

    public Cliente(String cpf, String nome, String email, LocalDate dataCadastro) {
        if (cpf == null || !cpf.trim().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        this.cpf = cpf.trim();
        this.nome = nome.trim();
        this.email = email.trim();
        this.dataCadastro = dataCadastro;
        this.historicoCompras = new ArrayList<>();
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void adicionarVenda(Venda venda) {
        if (venda != null) {
            historicoCompras.add(venda);
        }
    }

    public List<Venda> getHistoricoCompras() {
        return List.copyOf(historicoCompras);
    }
}
