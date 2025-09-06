package org.example.model.entity;

import org.example.util.StatusCorrida;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CorridaTest {

    @Test
    void deveCriarCorridaComStatusSolicitada() {
        Corrida corrida = new Corrida(1, "Centro", "Aeroporto", "Carro");

        assertEquals(1, corrida.getPassageiroId());
        assertEquals("Centro", corrida.getOrigem());
        assertEquals("Aeroporto", corrida.getDestino());
        assertEquals("Carro", corrida.getCategoriaVeiculo());
        assertEquals(StatusCorrida.SOLICITADA, corrida.getStatus());
    }

    @Test
    void devePermitirAlterarMotoristaEValor() {
        Corrida corrida = new Corrida(2, "Shopping", "Estação", "Moto");
        corrida.setMotoristaId(99);
        corrida.setValor(35.50);

        assertEquals(99, corrida.getMotoristaId());
        assertEquals(35.50, corrida.getValor());
    }

    @Test
    void devePermitirAlterarStatusHoraInicioHoraFim() {
        Corrida corrida = new Corrida(3, "Praça", "Universidade", "Van");
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
