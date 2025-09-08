package org.example.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.example.model.entity.CategoriaVeiculo.UBER_COMFORT;
import static org.example.model.entity.CategoriaVeiculo.UBER_X;
import static org.junit.jupiter.api.Assertions.*;

class CorridaTest {

    @Test
    void deveCriarCorridaComStatusSolicitada() {
        Corrida corrida = new Corrida(1, "Centro", "Aeroporto", UBER_COMFORT);

        assertEquals(1, corrida.getPassageiroId());
        assertEquals("Centro", corrida.getOrigem());
        assertEquals("Aeroporto", corrida.getDestino());
        assertEquals(UBER_COMFORT, corrida.getCategoriaVeiculo()); // <- corrigido
        assertEquals(StatusCorrida.SOLICITADA, corrida.getStatus());
    }

    @Test
    void devePermitirAlterarMotoristaEValor() {
        Corrida corrida = new Corrida(2, "Shopping", "Estação", UBER_X);
        corrida.setMotoristaId(99);
        corrida.setValor(35.50);

        assertEquals(99, corrida.getMotoristaId());
        assertEquals(35.50, corrida.getValor());
    }

    @Test
    void devePermitirAlterarStatusHoraInicioHoraFim() {
        Corrida corrida = new Corrida(3, "Praça", "Universidade", UBER_X);
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(25);

        corrida.setHoraInicio(inicio);
        corrida.setHoraFim(fim);
        corrida.setStatus(StatusCorrida.CONCLUIDA); // alterado para um status válido

        assertEquals(inicio, corrida.getHoraInicio());
        assertEquals(fim, corrida.getHoraFim());
        assertEquals(StatusCorrida.CONCLUIDA, corrida.getStatus());
    }
}
