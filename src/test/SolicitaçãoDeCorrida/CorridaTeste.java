package org.example.model.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CorridaTest {

    @Test
    void testConstrutorDefineValoresCorretos() {
        Corrida corrida = new Corrida(1, "Origem A", "Destino B", "Econômico");

        assertEquals(1, corrida.getPassageiroId());
        assertEquals("Origem A", corrida.getOrigem());
        assertEquals("Destino B", corrida.getDestino());
        assertEquals("Econômico", corrida.getCategoriaVeiculo());
        assertEquals(StatusCorrida.SOLICITADA, corrida.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        Corrida corrida = new Corrida(1, "Origem A", "Destino B", "Econômico");

        corrida.setId(100);
        corrida.setMotoristaId(200);
        corrida.setValor(50.75);
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(20);
        corrida.setHoraInicio(inicio);
        corrida.setHoraFim(fim);
        corrida.setCategoriaVeiculo("Luxo");
        corrida.setStatus(StatusCorrida.EM_ANDAMENTO);

        assertEquals(100, corrida.getId());
        assertEquals(200, corrida.getMotoristaId());
        assertEquals(50.75, corrida.getValor());
        assertEquals(inicio, corrida.getHoraInicio());
        assertEquals(fim, corrida.getHoraFim());
        assertEquals("Luxo", corrida.getCategoriaVeiculo());
        assertEquals(StatusCorrida.EM_ANDAMENTO, corrida.getStatus());
    }

    @Test
    void testAtualizacaoOrigemDestino() {
        Corrida corrida = new Corrida(1, "Origem A", "Destino B", "Econômico");

        corrida.setOrigem("Origem Nova");
        corrida.setDestino("Destino Novo");

        assertEquals("Origem Nova", corrida.getOrigem());
        assertEquals("Destino Novo", corrida.getDestino());
    }
}
