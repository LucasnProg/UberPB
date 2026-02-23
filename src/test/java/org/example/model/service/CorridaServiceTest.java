package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorridaServiceTest {

    private CorridaService corridaService;
    private MotoristaRepository motoristaRepo;
    private VeiculoRepository veiculoRepo;
    private PassageiroRepository passageiroRepo;

    @BeforeEach
    void setUp() {
        // Criar instâncias dos repositórios de teste
        motoristaRepo = new MotoristaRepository();
        veiculoRepo = new VeiculoRepository();
        passageiroRepo = new PassageiroRepository();
        corridaService = new CorridaService();

        // Aqui você pode adicionar lógica para limpar arquivos ou listas em memória, se necessário
    }

    @Test
    void testeFluxoCompletoDeCorrida() {
        // 1. Criar passageiro
        Passageiro passageiro = new Passageiro("Maria", "maria@email.com", "senha123", "12345678900", "11988888888");
        passageiroRepo.salvar(passageiro);

        // 2. Criar veículo compatível com motorista
        Veiculo veiculo = new Veiculo(
                "Toyota", "ModeloX", "ABC-1234", "123456789", 2020,
                "Preto", 450.0f, 4, false, CategoriaVeiculo.UBER_X
        );
        veiculoRepo.salvar(veiculo);

        // 3. Criar motorista
        Motorista motorista = new Motorista("João", "joao@email.com", "senha123", "98765432100", "11999999999");
        motorista.setIdVeiculo(veiculo.getId());
        motorista.setStatus(MotoristaStatus.DISPONIVEL);
        motoristaRepo.salvar(motorista);

        // 4. Solicitar corrida
        Localizacao origem = new Localizacao(0, 0);
        Localizacao destino = new Localizacao(5, 5);
        Corrida corrida = corridaService.solicitarCorrida(passageiro, origem, destino, CategoriaVeiculo.UBER_X,FormaPagamento.PIX);

        assertNotNull(corrida);
        assertEquals(StatusCorrida.SOLICITADA, corrida.getStatus());

        // 5. Aceitar corrida
        boolean aceite = corridaService.aceitarCorrida(motorista, corrida);
        assertTrue(aceite);

        Corrida corridaAtualizada = corridaService.buscarCorridaPorId(corrida.getId());
        assertEquals(StatusCorrida.EM_CURSO, corridaAtualizada.getStatus());
        assertEquals(motorista.getId(), corridaAtualizada.getMotoristaId());

        // 6. Iniciar corrida
        assertEquals(StatusCorrida.EM_CURSO, corridaService.buscarCorridaPorId(corrida.getId()).getStatus());
        assertNotNull(corridaAtualizada.getHoraInicio());

        // 7. Finalizar corrida
        corridaService.finalizarCorrida(corridaAtualizada);
        Corrida corridaFinalizada = corridaService.buscarCorridaPorId(corrida.getId());
        assertEquals(StatusCorrida.FINALIZADA, corridaFinalizada.getStatus());
        assertNotNull(corridaFinalizada.getHoraFim());

        // 8. Verificar atualização do passageiro e motorista
        Passageiro passageiroAtualizado = corridaService.getPassageiroById(passageiro.getId());
        Motorista motoristaAtualizado = corridaService.getMotoristaById(motorista.getId());

        assertTrue(passageiroAtualizado.getHistoricoCorridas().stream()
                .anyMatch(c -> c.getId() == corrida.getId()));
        assertEquals(MotoristaStatus.DISPONIVEL, motoristaAtualizado.getStatus());
    }
}
