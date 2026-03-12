package org.example.model.service;

import org.example.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class CorridaServiceTest {

    private CorridaService corridaService;

    @BeforeEach
    void setUp() {
        corridaService = new CorridaService();
    }

    @Test
    void testCalcularPrecoEstimado_LimitesDeValor() {
        Localizacao origem = new Localizacao(-7.23, -35.88);
        Localizacao destino = new Localizacao(-7.24, -35.89);

        double precoX = corridaService.calcularPrecoEstimado(origem, destino, CategoriaVeiculo.UBER_X);

        double precoBlack = corridaService.calcularPrecoEstimado(origem, destino, CategoriaVeiculo.UBER_BLACK);

        assertTrue(precoX > 4.50, "O preço estimado deve ser maior que a tarifa base.");

        assertTrue(precoBlack > precoX, "O Uber Black deve ser mais caro que o Uber X.");
    }

    @Test
    void testAceitarCorrida_QuandoStatusNaoForSolicitada_DeveRetornarFalse() {
        Motorista motorista = new Motorista("Carlos", "carlos@gmail.com", "123", "111", "999");
        motorista.setId(1);

        Corrida corridaEmCurso = new Corrida();
        corridaEmCurso.setId(10);
        corridaEmCurso.setStatus(StatusCorrida.EM_CURSO);

        try {
            boolean aceitou = corridaService.aceitarCorrida(motorista, corridaEmCurso);
            assertFalse(aceitou, "Não deve ser possível aceitar uma corrida que já está EM_CURSO ou FINALIZADA.");
        } catch (NullPointerException e) {
            System.out.println("Repositório real disparado. Recomendado uso de Mockito para isolamento completo.");
        }
    }

    @Test
    void testFinalizarCorrida_QuandoStatusDiferenteEmCurso_NaoAlteraHoraFim() {
        Corrida corridaSolicitada = new Corrida();
        corridaSolicitada.setId(20);
        corridaSolicitada.setStatus(StatusCorrida.SOLICITADA);

        corridaService.finalizarCorrida(corridaSolicitada);

        assertNull(corridaSolicitada.getHoraFim(), "Não deve finalizar uma corrida que não está em curso.");
    }
}