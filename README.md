# Plataforma de Pedidos Distribuída

Projeto demonstrativo de **Sistemas Distribuídos**, aplicando conceitos de comunicação assíncrona entre processos independentes utilizando **RabbitMQ** como middleware de mensageria.

> Desenvolvido para a disciplina de Sistemas Distribuídos — UFPI, sob orientação do Prof. Dr. José Torres Neto.

## Visão geral

A aplicação simula o fluxo de criação de um pedido em uma plataforma de e-commerce, onde múltiplos serviços independentes reagem ao mesmo evento sem conhecimento direto entre si. Cada serviço é um processo Spring Boot isolado, e a comunicação entre eles ocorre **exclusivamente via mensageria assíncrona**, nunca por chamada direta (REST/RPC) entre os serviços de domínio.

## Arquitetura

```
                                               ┌────────────────────┐
                         POST /api/pedidos --> │   PedidosService    │  (cliente / producer)
                                               └─────────┬───────────┘
                                                         │ publica
                                                         ▼
                                               ┌────────────────────┐
                                               │  pedidos-exchange   │  (Fanout Exchange)
                                               └─────────┬───────────┘
                                        ┌────────────────┼────────────────┐
                                        ▼                ▼                ▼
                              ┌──────────────┐  ┌────────────────┐  ┌──────────────────┐
                              │ Notificacao  │  │ ProcessarPaga-  │  │ ReservarEstoque  │
                              │   Service    │  │ mentoService    │  │     Service      │
                              └──────────────┘  └────────────────┘  └──────────────────┘
```

<div align="center">
  <img width="655" height="385" alt="image" src="https://github.com/user-attachments/assets/d28d3ec4-954b-48ee-986a-29dc2577edca" />
</div>

Cada serviço consumidor possui sua **própria fila**, vinculada à mesma exchange do tipo **fanout**. Isso significa que toda mensagem publicada é replicada e entregue a **todas as filas simultaneamente** — caracterizando um padrão **publish/subscribe**, em que os consumidores são desacoplados entre si e reagem de forma autônoma e paralela ao mesmo evento.

## Serviços

| Serviço | Papel | Fila |
|---|---|---|
| `PedidosService` | Cliente — expõe API REST e publica o evento de pedido criado | — (producer) |
| `ProcessarPagamentoService` | Consumidor — simula o processamento do pagamento | `pedidos-processar-pagamento-queue` |
| `ReservarEstoque` | Consumidor — simula a reserva de estoque do pedido | `pedidos-reservar-estoque-queue` |
| `NotificacaoService` | Consumidor — simula o envio de notificação ao cliente | `pedidos-notificacao-queue` |

## Tecnologias

- **Java 25** + **Spring Boot**
- **Spring AMQP** (`spring-rabbit`) para integração com o broker
- **RabbitMQ** como message broker (exchange do tipo *fanout*)
- **Gson** para serialização/desserialização do payload das mensagens
- **Docker Compose** para orquestração do RabbitMQ
- **Lombok** para redução de boilerplate (`@Getter`, `@Setter`, `@ToString`)

## Conceito de Sistemas Distribuídos em destaque

**Comunicação distribuída baseada em eventos (Event-Driven Communication) via Fanout Exchange.**

Ao publicar uma única mensagem na exchange `pedidos-exchange`, o RabbitMQ se encarrega de replicá-la para todas as filas vinculadas. Os serviços consumidores:
- Não se comunicam diretamente entre si;
- Não conhecem a existência um do outro;
- Processam o mesmo evento de forma independente, cada um cuidando de sua própria responsabilidade de negócio.

Esse modelo reduz o acoplamento entre os componentes do sistema e permite que novos consumidores sejam adicionados sem qualquer alteração no serviço produtor.

## Como executar

### Pré-requisitos
- Java 25+
- Maven
- Docker e Docker Compose

### 1. Subir o RabbitMQ

```bash
cd PedidosService
docker compose up -d
```

Verifique se o broker está no ar:
```bash
docker ps
```

O painel de gerenciamento do RabbitMQ fica disponível (se habilitado) em `http://localhost:15672`.

### 2. Compilar e executar cada serviço

Em terminais separados (cada serviço é um processo independente):

```bash
# Terminal 1
cd PedidosService
mvn spring-boot:run

# Terminal 2
cd ProcessarPagamentoService
mvn spring-boot:run

# Terminal 3
cd ReservarEstoque
mvn spring-boot:run

# Terminal 4
cd NotificacaoService
mvn spring-boot:run
```

### 3. Criar um pedido

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
        "nome": "Mouse Gamer RGB",
        "quantidade": 2,
        "tipo": "ELETRONICO",
        "valorUnitario": 89.90,
        "valorFrete": 15.00,
        "status": "PENDENTE"
      }'
```

Ao enviar a requisição, observe os logs dos três serviços consumidores — todos devem reagir ao mesmo evento de pedido criado.

## Decisões de projeto e desafios enfrentados

- **Entidade `Pedido` duplicada em cada serviço**, em vez de um módulo compartilhado (`common`). Optou-se por essa abordagem pela simplicidade do projeto, já que o Gson desserializa o payload pela estrutura do JSON, não pela identidade da classe — cada serviço mantém seu próprio "contrato" do evento, de forma desacoplada.
- **Configuração de portas no Docker Compose**: o uso de `ports: - '5672'` (sem mapeamento fixo `host:container`) inicialmente impedia a conexão dos serviços executados fora do container, resultando em `AmqpConnectException`. Corrigido fixando o mapeamento `'5672:5672'`.
- **Serialização do campo `Instant`**: testou-se inicialmente o uso de Jackson sem o módulo `jackson-datatype-jsr310`, o que gerava uma representação não legível do timestamp. Optou-se pelo uso do Gson como solução mais simples para o escopo do projeto.
