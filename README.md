# UberPB 🚗💨

O **UberPB** é uma aplicação de simulação de transporte de passageiros e motoristas desenvolvida em Java. O sistema opera inteiramente via Interface de Linha de Comando (CLI), permitindo o gerenciamento completo de corridas, usuários e veículos de forma eficiente e modular.

## 📑 Índice
- [Visão Geral](#-visão-geral)
- [Recursos Principais](#-recursos-principais)
- [Arquitetura e Tecnologias](#-arquitetura-e-tecnologias)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar](#-como-executar)
- [Histórico de Desenvolvimento](#-histórico-de-desenvolvimento)
- [Equipe](#-equipe)

## 🌟 Visão Geral
O sistema foi projetado para simular o ecossistema de transporte por aplicativo, gerenciando entidades fundamentais como Passageiros, Motoristas, Gerentes e Veículos. A persistência de dados é feita em arquivos locais (JSON), o que elimina a necessidade de bancos de dados externos e facilita a implantação imediata.

## 🚀 Recursos Principais
* **Gestão de Usuários:** CRUD completo e autenticação para Passageiros, Motoristas e Gerentes.
* **Simulação de Corridas:** Solicitação com escolha de categoria, cálculo automático de preço e estimativas.
* **Acompanhamento em Tempo Real:** Visualização de rotas e estimativa de chegada do motorista.
* **Sistema de Pagamentos:** Integração com múltiplas formas de pagamento (Cartão, PayPal, Dinheiro) e emissão de recibos eletrônicos.
* **Logística:** Atribuição de corridas baseada na proximidade do motorista e localização.

## 🏗 Arquitetura e Tecnologias
O projeto segue o padrão **MVC (Model-View-Controller)** para garantir a separação de responsabilidades e facilidade de manutenção.

* **Linguagem:** Java 22.
* **Persistência:** Arquivos JSON locais com suporte da biblioteca **Google Gson (2.13.0)**.
* **Testes:** Framework **JUnit Jupiter (5.9.3)** para validação das regras de negócio.
* **Gerenciador de Dependências:** Maven.

### Detalhamento MVC:
* **View:** Interface baseada em Terminal (CLI). A classe `Main.java` inicia a experiência do usuário.
* **Controller:** A classe `Sistema.java` atua como o controlador principal, gerenciando o estado da sessão e o fluxo da aplicação.
* **Model:** Camada abrangente que inclui Entidades (estrutura), Serviços (regras de negócio) e Repositórios (persistência).

## 📁 Estrutura do Projeto
```text
src/main/java/org/example/
├── model/
│   ├── entity/      # Definição de classes e Enums (Usuario, Corrida, etc.)
│   ├── service/     # Lógica de negócio e regras do sistema
│   └── repository/  # Camada de acesso e persistência de dados (JSON)
├── util/            # Classes utilitárias e tratamento de exceções
├── view/            # Interfaces de interação via console (CLI)
└── Main.java        # Ponto de entrada do sistema
