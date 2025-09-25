package org.example.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.example.model.entity.CategoriaVeiculo.UBER_COMFORT;
import static org.example.model.entity.CategoriaVeiculo.UBER_X;
import static org.junit.jupiter.api.Assertions.*;

class CorridaTest {

    @Test
    void deveCriarCorridaComStatusSolicitada() {
        // Criando Localizacao com coordenadas
        Localizacao origem = new Localizacao(-23.5505, -46.6333); // exemplo São Paulo
        origem.setDescricao("Centro");

        Localizacao destino = new Localizacao(-23.4356, -46.4731); // exemplo Aeroporto
        destino.setDescricao("Aeroporto");

        Corrida corrida = new Corrida(1, origem, destino, UBER_COMFORT);

        assertEquals(1, corrida.getPassageiroId());
        assertEquals(origem, corrida.getOrigem());
        assertEquals(destino, corrida.getDestino());
        assertEquals(UBER_COMFORT, corrida.getCategoriaVeiculo());
        assertEquals(StatusCorrida.SOLICITADA, corrida.getStatus());
    }

    @Test
    void devePermitirAlterarMotoristaEValor() {
        Localizacao origem = new Localizacao(-23.6000, -46.6500);
        Localizacao destino = new Localizacao(-23.7000, -46.7000);

        Corrida corrida = new Corrida(2, origem, destino, UBER_X);
        corrida.setMotoristaId(99);
        corrida.setValor(35.50);

        assertEquals(99, corrida.getMotoristaId());
        assertEquals(35.50, corrida.getValor());
    }

    @Test
    void devePermitirAlterarStatusHoraInicioHoraFim() {
        Localizacao origem = new Localizacao(-23.5000, -46.6000);
        Localizacao destino = new Localizacao(-23.6500, -46.7500);

        Corrida corrida = new Corrida(3, origem, destino, UBER_X);
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(25);

        corrida.setHoraInicio(inicio);
        corrida.setHoraFim(fim);
        corrida.setStatus(StatusCorrida.FINALIZADA);

        assertEquals(inicio, corrida.getHoraInicio());
        assertEquals(fim, corrida.getHoraFim());
        assertEquals(StatusCorrida.FINALIZADA, corrida.getStatus());
    }
}
