package org.example.model.entity;

/**
 * Enum que define os possíveis status de uma Corrida/Pedido durante seu ciclo de vida.
 *
 * Observação:
 * - O projeto reutiliza este enum tanto para Corrida quanto para Pedido.
 * - Para PEDIDOS, foram adicionados status intermediários para permitir
 *   acompanhamento detalhado pelo Cliente no CLI.
 */
public enum StatusCorrida {
    // Pedido/Corrida criado(a) e aguardando aceite
    SOLICITADA,

    // Pedido/Corrida aceita por Motorista/Entregador
    ACEITA,

    // (Pedidos) Restaurante recebeu/aceitou e está preparando
    EM_PREPARO,

    // (Pedidos) Pedido saiu do restaurante e está a caminho do cliente
    SAIU_PARA_ENTREGA,

    // Viagem/Entrega em andamento
    EM_CURSO,

    // Finalizado(a)
    FINALIZADA,

    // Cancelado(a)
    CANCELADA
}