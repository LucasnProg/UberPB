package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.StatusCorrida;
import org.example.model.repository.CorridaRepository;
import org.example.util.CrudUserError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CorridaServiceTest {

    private CorridaService corridaService;
    private CorridaRepository corridaRepositoryMock;
    private MotoristaService motoristaServiceMock;

    @BeforeEach
    void setup() {
        corridaRepositoryMock = mock(CorridaRepository.class);
        motoristaServiceMock = mock(MotoristaService.class);

        corridaService = new CorridaService() {
            @Override
            public void aceitarCorrida(Corrida corrida, String cpfMotorista) {
                this.corridaRepository = corridaRepositoryMock;
                this.motoristaService = motoristaServiceMock;
                super.aceitarCorrida(corrida, cpfMotorista);
            }

            @Override
            public void finalizarCorrida(Corrida corrida) {
                this.corridaRepository = corridaRepositoryMock;
                super.finalizarCorrida(corrida);
            }
        };
    }

    @Test
    void testAceitarCorridaComSucesso() {
        Corrida corrida = new Corrida(1, "Centro", "Shopping", "Economico");

        Motorista motorista = new Motorista("123", "João", "Carro X");
        motorista.setId(10);

        when(motoristaServiceMock.getMotorista("123")).thenReturn(motorista);
        when(corridaRepositoryMock.carregar()).thenReturn(new ArrayList<>());

        corridaService.aceitarCorrida(corrida, "123");

        assertEquals(StatusCorrida.ACEITA, corrida.getStatus());
        assertEquals(10, corrida.getMotoristaId());

        verify(corridaRepositoryMock).salvar(anyList());
    }

    @Test
    void testAceitarCorridaJaAceitaLancaErro() {
        Corrida corrida = new Corrida(1, "Centro", "Shopping", "Economico");
        corrida.setStatus(StatusCorrida.ACEITA);

        assertThrows(CrudUserError.class,
                () -> corridaService.aceitarCorrida(corrida, "123"));
    }

    @Test
    void testFinalizarCorridaComSucesso() {
        Corrida corrida = new Corrida(1, "Centro", "Shopping", "Economico");
        corrida.setId(1);
        corrida.setStatus(StatusCorrida.ACEITA);

        List<Corrida> corridasSalvas = new ArrayList<>();
        corridasSalvas.add(corrida);

        when(corridaRepositoryMock.carregar()).thenReturn(corridasSalvas);

        corridaService.finalizarCorrida(corrida);

        assertEquals(StatusCorrida.CONCLUIDA, corrida.getStatus());
        assertNotNull(corrida.getHoraFim());
        verify(corridaRepositoryMock).salvar(corridasSalvas);
    }

    @Test
    void testFinalizarCorridaNaoAceitaLancaErro() {
        Corrida corrida = new Corrida(1, "Centro", "Shopping", "Economico");
        corrida.setStatus(StatusCorrida.SOLICITADA);

        assertThrows(CrudUserError.class,
                () -> corridaService.finalizarCorrida(corrida));
    }

    @Test
    void testCalcularPrecoEconomico() throws Exception {
        // Usamos reflection para acessar método privado
        var method = CorridaService.class.getDeclaredMethod("calcularPreco", String.class, String.class, String.class);
        method.setAccessible(true);

        double preco = (double) method.invoke(corridaService, "Centro", "Shopping", "Economico");
        assertTrue(preco > 0);
    }

    @Test
    void testCalcularPrecoLuxoMaisCaro() throws Exception {
        var method = CorridaService.class.getDeclaredMethod("calcularPreco", String.class, String.class, String.class);
        method.setAccessible(true);

        double precoEconomico = (double) method.invoke(corridaService, "Centro", "Shopping", "Economico");
        double precoLuxo = (double) method.invoke(corridaService, "Centro", "Shopping", "Luxo");

        assertTrue(precoLuxo > precoEconomico);
    }
}
