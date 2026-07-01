```Mermaid```
classDiagram
direction TB
    class Main {
	    - List~Funcionario~ funcionarios
	    - List~ItemEstoque~ estoque
	    - List~Cliente~ clientes
	    void
	    void
	    + main(String[])
	    + inicializarDadosPadrao()
    }

    class PersistenciaService {
	    - Path BASE_DIR
	    - Path ARQUIVO_FUNCIONARIOS
	    - Path ARQUIVO_ESTOQUE
	    - Path ARQUIVO_CLIENTES
	    void
	    void
	    void
	    void
	    void
	    void
	    void
	    void
	    + carregarDados()
	    + salvarDados()
	    + carregarFuncionarios()
	    + carregarEstoque()
	    + carregarClientes()
	    + salvarFuncionarios()
	    + salvarEstoque()
	    + salvarClientes()
    }

    class MenuGerente {
	    void
	    void
	    void
	    void
	    void
	    + menuGerente(Scanner)
	    + cadastrarNovoFuncionario(Scanner)
	    + demitirFuncionario(Scanner)
	    + listarFuncionarios()
	    + listarCargos()
    }

    class MenuFuncionario {
	    void
	    void
	    void
	    void
	    void
	    + menuFuncionario(Scanner)
	    + menuVenda(Scanner, Funcionario)
	    + menuEstoque(Scanner, Funcionario)
	    + consultarItemEstoque(String)
	    + cadastrarCliente(Scanner)
    }

    class EstoqueService {
	    void
	    + listaEstoque()
    }

    class VendaService {
	    void
	    String
	    String
	    + gerarNotaFiscal(Cliente, List~ItemCarrinho~, Pagamento, Funcionario)
	    + gerarCodigoVenda()
	    + formatarCpf(String)
    }

    class Funcionario {
	    # String matricula
	    # String nome
	    # String cpf
	    # boolean ativo
	    # List~RegistroPonto~ registrosPonto
	    void
	    String
	    String
	    void
	    String
	    boolean
	    + adicionarRegistro(RegistroPonto)
	    + getRegistrosPonto() List~RegistroPonto~
	    + getMatricula()
	    + getCpf()
	    + setMatricula(String)
	    + getNome()
	    + isAtivo()
	    + getPermissoes() Set~Permissao~
    }

    class Gerente {
	    + Gerente(String, String, String)
    }

    class Vendedor {
	    + Vendedor(String, String, String)
    }

    class Estoquista {
	    - boolean habilitadoEmpilhadeira
	    boolean
	    + Estoquista(String, String, String, boolean)
	    + podeMovimentarItemPesado()
    }

    class RegistroPonto {
	    - String codigo
	    - LocalDateTime dataHora
	    - TipoRegistro tipo
	    - String justificativa
    }

    class Permissao {
	    APROVAR_DESCONTO
	    CADASTRAR_FUNCIONARIO
	    CANCELAR_VENDA
	    ALTERAR_ESTOQUE
	    VISUALIZAR_RELATORIOS
	    REALIZAR_VENDA
	    APLICAR_DESCONTO_PADRAO
	    CONSULTAR_ESTOQUE
	    CADASTRAR_PRODUTO
	    APROVAR_PEDIDO
	    ATUALIZAR_ESTOQUE
    }

    class TipoRegistro {
	    ENTRADA
	    SAIDA
	    INTERVALO_INICIO
	    INTERVALO_FIM
    }

    class ItemEstoque {
	    # String codigo
	    # String titulo
	    # int quantidadeAtual
	    # int quantidadeMinima
	    # double precoCusto
	    # double precoVenda
	    # LocalDate dataCadastro
	    boolean
	    String
	    int
	    + precisaReposicao()
	    + getCategoria()
	    + getPrazoDevolucaoDias()
    }

    class Livro {
	    - String isbn
	    - String autor
	    - String editora
	    - int edicao
	    - String genero
    }

    class Eletronico {
	    - int garantiaMeses
	    - String numeroSerie
	    + Eletronico()
	    + Eletronico(int, String)
    }

    class Papelaria {
	    - String marca
	    - boolean materialEscolar
    }

    class Cliente {
	    - String cpf
	    - String nome
	    - String email
	    - LocalDate dataCadastro
	    - List~Venda~ historicoCompras
	    void
	    + Cliente(String, String, String, LocalDate)
	    + adicionarVenda(Venda)
    }

    class Venda {
	    - String codigoVenda
	    - LocalDateTime dataVenda
	    - double descontoAplicado
	    - StatusVenda status
	    - Funcionario funcionario
	    - Cliente cliente
	    - Map~ItemEstoque, Integer~ itensVenda
	    - Pagamento pagamento
	    void
	    double
	    void
	    void
	    void
	    void
	    void
	    void
	    + Venda(String, Cliente, Funcionario)
	    + adicionarItem(ItemEstoque, int)
	    + calcularTotal()
	    + aplicarDesconto(double)
	    + setPagamento(Pagamento)
	    + finalizar()
	    + cancelar()
	    + devolverParcial()
	    + devolverTotal()
    }

    class VendaRepository {
	    void
	    + salvarVenda(Venda)
    }

    class Pagamento {
	    # double valorTotal
	    # LocalDateTime dataPagamento
	    # StatusPagamento status
	    void
	    String
	    + processar(double)
	    + getMetodoPagamento()
    }

    class PagamentoDinheiro {
	    - double valorRecebido
	    - double troco
    }

    class PagamentoPix {
	    - String chavePix
    }

    class StatusVenda {
	    FINALIZADA
	    CANCELADA
	    DEVOLVIDA_PARCIAL
	    DEVOLVIDA_TOTAL
    }

    class StatusPagamento {
	    PENDENTE
	    APROVADO
	    RECUSADO
    }

    class ClienteNaoEncontradoException {
    }

    class EstoqueInsuficienteException {
    }

    class VendaInvalidaException {
    }

    class PagamentoDin {
    }

	<<application>> Main
	<<abstract>> Funcionario
	<<enumeration>> Permissao
	<<enumeration>> TipoRegistro
	<<abstract>> ItemEstoque
	<<abstract>> Pagamento
	<<enumeration>> StatusVenda
	<<enumeration>> StatusPagamento
	<<exception>> ClienteNaoEncontradoException
	<<exception>> EstoqueInsuficienteException
	<<exception>> VendaInvalidaException

    Main --> PersistenciaService
    Main --> MenuGerente
    Main --> MenuFuncionario
    Main --> EstoqueService
    Main --> VendaService
    Main "1" *-- "0..*" Funcionario
    Main "1" *-- "0..*" ItemEstoque
    Main "1" *-- "0..*" Cliente
    MenuGerente ..> PersistenciaService
    MenuGerente ..> Funcionario
    MenuFuncionario ..> PersistenciaService
    MenuFuncionario ..> VendaRepository
    MenuFuncionario ..> VendaService
    MenuFuncionario ..> PagamentoPix
    MenuFuncionario ..> PagamentoDinheiro
    MenuFuncionario ..> PagamentoDin
