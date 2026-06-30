package venda;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import item.estoque.ItemEstoque;
import funcionario.Funcionario;
import enums.StatusVenda;
import exceptions.VendaInvalidaException;

public class Venda {

    private String codigoVenda;
    private LocalDateTime dataVenda;
    private double descontoAplicado;
    private StatusVenda status;
    private Funcionario funcionario;
    private Cliente cliente;
    private Map<ItemEstoque, Integer> itensVenda;
    private Pagamento pagamento;

    public Venda(String codigoVenda, Cliente cliente, Funcionario funcionario) {
        this.codigoVenda = codigoVenda;
        this.dataVenda = LocalDateTime.now();
        this.descontoAplicado = 0.0;
        this.status = null;
        this.funcionario = funcionario;
        this.cliente = cliente;
        this.itensVenda = new LinkedHashMap<>();
    }

    public void adicionarItem(ItemEstoque item, int quantidade) {
        if (item == null || quantidade <= 0) {
            return;
        }
        itensVenda.merge(item, quantidade, Integer::sum);
    }

    public double calcularTotal() {
        return itensVenda.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecoVenda() * entry.getValue())
                .sum() - descontoAplicado;
    }

    public void aplicarDesconto(double desconto) {
        if (desconto > 0) {
            this.descontoAplicado = desconto;
        }
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public void finalizar() throws VendaInvalidaException {
        if (status == StatusVenda.FINALIZADA || status == StatusVenda.CANCELADA
                || status == StatusVenda.DEVOLVIDA_PARCIAL || status == StatusVenda.DEVOLVIDA_TOTAL) {
            throw new VendaInvalidaException("Essa venda já não pode ser finalizada.");
        }
        if (pagamento == null || itensVenda.isEmpty()) {
            throw new VendaInvalidaException("Não é possível finalizar uma venda sem pagamento ou itens.");
        }

        this.status = StatusVenda.FINALIZADA;
        if (cliente != null) {
            cliente.adicionarVenda(this);
        }
    }

    public void cancelar() throws VendaInvalidaException {
        if (status == StatusVenda.FINALIZADA || status == StatusVenda.CANCELADA
                || status == StatusVenda.DEVOLVIDA_PARCIAL || status == StatusVenda.DEVOLVIDA_TOTAL) {
            throw new VendaInvalidaException("Essa venda não pode ser cancelada no estado atual.");
        }

        this.status = StatusVenda.CANCELADA;
    }

    public void devolverParcial() throws VendaInvalidaException {
        if (status != StatusVenda.FINALIZADA) {
            throw new VendaInvalidaException("A devolução parcial só é permitida para vendas finalizadas.");
        }

        this.status = StatusVenda.DEVOLVIDA_PARCIAL;
    }

    public void devolverTotal() throws VendaInvalidaException {
        if (status != StatusVenda.FINALIZADA) {
            throw new VendaInvalidaException("A devolução total só é permitida para vendas finalizadas.");
        }

        this.status = StatusVenda.DEVOLVIDA_TOTAL;
    }

    public String getCodigoVenda() {
        return codigoVenda;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public double getDescontoAplicado() {
        return descontoAplicado;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Map<ItemEstoque, Integer> getItensVenda() {
        return Map.copyOf(itensVenda);
    }

    public Pagamento getPagamento() {
        return pagamento;
    }
}
