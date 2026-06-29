# Sistema de Gestão de Vendas - LP2

Sistema Java para gerenciamento de vendas em lojas, desenvolvido como trabalho de Linguagem de Programação 2.

## 📋 Requisitos

### Versão do Java
- **Java 11+** (recomendado Java 17 ou superior)
- Verifique sua versão: `java -version`

### Dependências
Este projeto **não possui dependências externas**. Utiliza apenas a biblioteca padrão do Java.

## 🚀 Como Executar

### 1. Clonar o Repositório
```bash
git clone https://github.com/caiocesarmb/Trabalho-de-LP-2.git
cd Trabalho-de-LP-2
```

### 2. Compilar o Projeto
```bash
javac -d . $(find . -name '*.java' | sort)
```

Isso compilará todos os arquivos `.java` e gerará os arquivos `.class` na mesma estrutura de diretórios.

### 3. Executar a Aplicação
```bash
java -cp . main.Main
```

## 📁 Estrutura do Projeto

```
Trabalho-de-LP-2/
├── README.md                 # Este arquivo
├── Diagrama-UML.md          # Diagrama UML do projeto
├── main/
│   └── Main.java            # Classe principal da aplicação
├── funcionario/             # Classes de funcionários
│   ├── Funcionario.java     # Classe abstrata base
│   ├── Vendedor.java
│   ├── Gerente.java
│   └── Estoquista.java
├── item/estoque/            # Classes de produtos
│   ├── ItemEstoque.java     # Classe abstrata base
│   ├── Livro.java
│   ├── Eletronico.java
│   └── Papelaria.java
├── venda/                   # Sistema de vendas
│   ├── Venda.java
│   ├── Cliente.java
│   ├── Pagamento.java
│   ├── PagamentoDinheiro.java
│   ├── PagamentoPix.java
│   ├── VendaRepository.java
│   └── vendas.txt           # Log de vendas (gerado em runtime)
├── permissao/               # Controle de permissões
│   └── Permissao.java
├── enums/                   # Enumerações
│   ├── StatusVenda.java
│   └── StatusPagamento.java
└── registro/                # Registro de pontos
    ├── RegistroPonto.java
    └── TipoRegistro.java
```

## 💻 Funcionalidades

- ✅ Cadastro de clientes
- ✅ Seleção automática de vendedores
- ✅ Catálogo de produtos (Livros, Eletrônicos, Papelaria)
- ✅ Adição de múltiplos produtos à venda
- ✅ Aplicação de descontos
- ✅ Múltiplas formas de pagamento (Dinheiro e PIX)
- ✅ Registro de vendas em arquivo
- ✅ Controle de permissões por perfil

## 🎯 Como Usar a Aplicação

### Na Execução:

1. **Cadastre os dados do cliente:**
   - CPF
   - Nome
   - Email

2. **Selecione os produtos:**
   - Digite o número do produto desejado
   - Informe a quantidade
   - Escolha se deseja adicionar outro produto

3. **Aplique desconto (opcional):**
   - Sistema perguntará se deseja desconto

4. **Escolha a forma de pagamento:**
   - Opção 1: Dinheiro
   - Opção 2: PIX

5. **Confirme o pagamento:**
   - Digite "s" para confirmar
   - A venda será registrada

## 🔧 Compilação Alternativa

Para compilar apenas um arquivo específico:
```bash
javac arquivo.java
```

Para compilar em diretório específico:
```bash
javac -d ./build $(find . -name '*.java')
cd build
java -cp . main.Main
```

## 📝 Logs de Vendas

As vendas são salvas automaticamente em `vendas.txt` no diretório raiz do projeto com o seguinte formato:
```
Venda#CodigoVenda#Cliente#Total#DataHora#Status
```

## ⚠️ Notas Importantes

- Não é necessário instalar dependências externas
- O projeto utiliza apenas bibliotecas padrão do Java
- Certifique-se de ter permissão de escrita no diretório para gerar o arquivo de vendas
- Use `Ctrl+C` para sair da aplicação

## 👨‍💻 Desenvolvido por

Trabalho acadêmico de Linguagem de Programação 2

## 🔗 Links Úteis

- **Repositório:** [GitHub - Trabalho-de-LP-2](https://github.com/caiocesarmb/Trabalho-de-LP-2)
- **Diagrama UML:** [Diagrama-UML.md](./Diagrama-UML.md)

## 📅 Data

21 de Junho de 2026