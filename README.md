# Sistema Biblioteca — Projeto Integrador

**Status:** Em desenvolvimento

## Descrição e objetivo

O Sistema Biblioteca é uma aplicação desktop desenvolvida como parte do Projeto Integrador. Seu objetivo é apoiar o gerenciamento de uma biblioteca, organizando o cadastro de livros e leitores e o controle de empréstimos e devoluções.

## Tecnologias utilizadas

- Java 8
- Java Swing (interface gráfica)
- Apache NetBeans 8.2
- Apache Ant (compilação do projeto)
- MySQL (banco de dados relacional)
- JDBC / MySQL Connector/J (conexão entre Java e MySQL)
- Git e GitHub (versionamento)

## Equipe de desenvolvimento

- Marcos Laureano — desenvolvimento e testes do projeto.

## Funcionalidades e requisitos

- Cadastrar, consultar, editar e excluir livros.
- Cadastrar, consultar, editar e excluir leitores.
- Registrar empréstimos de livros para leitores cadastrados.
- Registrar devoluções de livros.
- Consultar empréstimos e seus respectivos status.
- Manter histórico de empréstimos e devoluções.
- Validar informações obrigatórias e impedir operações incompatíveis com o histórico de empréstimos.
- Armazenar os dados de forma persistente em um banco MySQL.

## Estrutura principal

- `src/`: código-fonte Java.
- `lib/`: bibliotecas utilizadas pelo projeto.
- `sql/`: script de criação e inicialização do banco de dados.
- `config/`: configurações locais (credenciais não são versionadas).
- `build.xml`: configuração de compilação com Ant.

## Como executar

1. Abra o projeto no NetBeans com Java 8 configurado.
2. Inicie o MySQL e execute o script SQL da pasta `sql/` para criar o banco e as tabelas.
3. Crie localmente o arquivo `config/banco.properties` com as configurações de conexão do seu ambiente, sem publicá-lo no GitHub.
4. Confira se o driver MySQL Connector/J está disponível na pasta `lib/` e no classpath do projeto.
5. Compile e execute o projeto pelo NetBeans.

> **Segurança:** arquivos `banco.properties` e artefatos de compilação são ignorados pelo Git. Nunca publique senhas reais no repositório.

## Versionamento

Este projeto utiliza Git e GitHub para registrar alterações por meio de commits e sincronizá-las com o repositório remoto por meio de push e pull.
