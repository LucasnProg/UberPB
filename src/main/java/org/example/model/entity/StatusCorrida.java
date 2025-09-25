package org.example.model.entity;

/**
 * Enum que define os possíveis status de uma Corrida durante seu ciclo de vida.
 */
public enum StatusCorrida {
    SOLICITADA,
    ACEITA,
    EM_CURSO,
    FINALIZADA, // Padronizado de CONCLUIDA
    CANCELADA
}