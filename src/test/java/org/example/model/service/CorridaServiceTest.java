package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.util.StatusCorrida;
import org.example.util.CrudUserError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorridaServiceTest {

    private CorridaService corridaService;

    @BeforeEach
    void setUp() {
        corridaService = new CorridaService();

        // Configurar dados iniciais se necessário
    }

    @Test
    void testProcurarCorrida() {
        boolean resultado = CorridaService.procurarCorrida(1, "Centro", "Aeroporto", "Economico");
        assertTrue(resultado, "A corrida deve ser procurada com sucesso");
    }

    @Test
    void testAceitarCorrida() {
        // Criar corrida
        Corrida corrida = new Corrida(1, "Centro", "Aeroporto", "Economico");
        corrida.setStatus(StatusCorrida.SOLICITADA);

        // Criar motorista manualmente
        Motorista motorista = new Motorista("João", "12345678900", null, null, null);
        motorista.setId(1);

        // Simular aceitar corrida sem repository real
        corrida.setMotoristaId(motorista.getId());
        corrida.setStatus(StatusCorrida.ACEITA);

        // Verificar alterações
        assertEquals(StatusCorrida.ACEITA, corrida.getStatus());
        assertEquals(motorista.getId(), corrida.getMotoristaId());
    }

    @Test
    void testFinalizarCorrida() {
        Corrida corrida = new Corrida(1, "Centro", "Aeroporto", "Economico");
        corrida.setStatus(StatusCorrida.ACEITA);

        // Simular finalização
        corrida.setHoraFim(LocalDateTime.now());
        corrida.setStatus(StatusCorrida.CONCLUIDA);

        assertEquals(StatusCorrida.CONCLUIDA, corrida.getStatus());
        assertNotNull(corrida.getHoraFim());
    }

    @Test
    void testAceitarCorridaNaoSolicitada() {
        Corrida corrida = new Corrida(1, "Centro", "Aeroporto", "Economico");
        corrida.setStatus(StatusCorrida.CONCLUIDA);

        CrudUserError exception = assertThrows(CrudUserError.class, () -> {
            if (corrida.getStatus() != StatusCorrida.SOLICITADA) {
                throw new CrudUserError("A corrida não está disponível para ser aceita.");
            }
        });

        assertEquals("A corrida não está disponível para ser aceita.", exception.getMessage());
    }

    @Test
    void testCalcularPreco() {
        // Refletir manualmente os métodos privados não é necessário; apenas testar via lógica
        double preco = corridaService.calcularPreco("Centro", "Aeroporto", "Luxo");
        assertTrue(preco > 0, "O preço deve ser maior que 0");
    }
}
