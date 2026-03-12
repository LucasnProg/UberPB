package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Corrida")
class CorridaTest {

    private Corrida corrida;
    private Localizacao origem;
    private Localizacao destino;

    @BeforeEach
    void setUp() {
        origem = new Localizacao(-7.22, -35.88);
        destino = new Localizacao(-7.23, -35.89);
        corrida = new Corrida(101, origem, destino, CategoriaVeiculo.UBER_COMFORT);
    }

    @Test
    @DisplayName("Deve validar o construtor principal e os valores iniciais")
    void testeConstrutorPrincipal() {
        assertAll("Verificação de estado inicial",
                () -> assertEquals(101, corrida.getPassageiroId()),
                () -> assertEquals(origem, corrida.getOrigem()),
                () -> assertEquals(destino, corrida.getDestino()),
                () -> assertEquals(CategoriaVeiculo.UBER_COMFORT, corrida.getCategoriaVeiculo()),
                () -> assertEquals(StatusCorrida.SOLICITADA, corrida.getStatus()),
                () -> assertNotNull(corrida.getMotoristasQueRejeitaram()),
                () -> assertTrue(corrida.getMotoristasQueRejeitaram().isEmpty())
        );
    }

    @Test
    @DisplayName("Deve garantir o funcionamento de todos os Getters e Setters")
    void testeGettersESetters() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(15);
        List<Integer> rejeicoes = new ArrayList<>();
        rejeicoes.add(50);

        corrida.setId(1);
        corrida.setPassageiroId(200);
        corrida.setMotoristaId(300);
        corrida.setValor(25.50);
        corrida.setHoraInicio(inicio);
        corrida.setHoraFim(fim);
        corrida.setStatus(StatusCorrida.EM_CURSO);
        corrida.setFormaPagamento(FormaPagamento.PIX);
        corrida.setPrecisaTroco(true);
        corrida.setMotoristasQueRejeitaram(rejeicoes);

        assertAll("Verificação de todos os campos via Getters",
                () -> assertEquals(1, corrida.getId()),
                () -> assertEquals(200, corrida.getPassageiroId()),
                () -> assertEquals(300, corrida.getMotoristaId()),
                () -> assertEquals(25.50, corrida.getValor()),
                () -> assertEquals(inicio, corrida.getHoraInicio()),
                () -> assertEquals(fim, corrida.getHoraFim()),
                () -> assertEquals(StatusCorrida.EM_CURSO, corrida.getStatus()),
                () -> assertEquals(FormaPagamento.PIX, corrida.getFormaPagamento()),
                () -> assertTrue(corrida.isPrecisaTroco()),
                () -> assertEquals(rejeicoes, corrida.getMotoristasQueRejeitaram())
        );
    }

    @Test
    @DisplayName("Deve adicionar um motorista à lista de rejeição corretamente")
    void testeAdicionarRejeicao() {
        corrida.adicionarRejeicao(5);
        corrida.adicionarRejeicao(12);

        List<Integer> lista = corrida.getMotoristasQueRejeitaram();

        assertAll("Verificação da lista de rejeições",
                () -> assertEquals(2, lista.size()),
                () -> assertTrue(lista.contains(5)),
                () -> assertTrue(lista.contains(12))
        );
    }

    @Test
    @DisplayName("Deve validar o construtor padrão (vazio)")
    void testeConstrutorPadrao() {
        Corrida corridaVazia = new Corrida();
        assertNotNull(corridaVazia);
        assertEquals(0, corridaVazia.getId());
        assertNull(corridaVazia.getOrigem());
    }
}