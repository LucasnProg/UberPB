package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {

    private PedidoService pedidoService;
    private PassageiroRepository passageiroRepository;
    private RestauranteRepository restauranteRepository;
    private EntregadorRepository entregadorRepository;
    private PedidoRepository pedidoRepository;

    private Passageiro passageiro;
    private Restaurante restaurante;
    private Entregador entregador;

    @BeforeEach
    void setup() {

        pedidoService = new PedidoService();
        passageiroRepository = new PassageiroRepository();
        restauranteRepository = new RestauranteRepository();
        entregadorRepository = new EntregadorRepository();
        pedidoRepository = new PedidoRepository();

        // Criar Passageiro
        passageiro = new Passageiro("Cliente Teste", "cliente@test.com", "123", "11111111111", "99999999");
        passageiroRepository.salvar(passageiro);

        // Criar Restaurante
        restaurante = new Restaurante(
                "Restaurante Teste",
                "rest@test.com",
                "123",
                "22222222222",
                "88888888",
                "Comida",
                new Localizacao(-7.2, -35.9)
        );
        restauranteRepository.salvar(restaurante);

        // Criar Entregador
        entregador = new Entregador("Entregador Teste", "ent@test.com", "123", "33333333333", "7777777");
        entregador.setLocalizacaoAtual(new Localizacao(-7.2, -35.9));
        entregadorRepository.salvar(entregador);
    }

    @Test
    void realizarPedido_deveCriarPedidoComStatusSolicitada() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Pizza", "Queijo", 50.0, 20));

        Pedido pedido = pedidoService.realizarPedido(
                passageiro.getId(),
                restaurante.getId(),
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.25, -35.95),
                FormaPagamento.CARTAO,
                itens,
                null
        );

        assertNotNull(pedido);
        assertEquals(StatusCorrida.SOLICITADA, pedido.getStatusPedido());
        assertTrue(pedido.getValor() > 0);
    }

    @Test
    void cancelarPedido_deveAlterarStatusParaCancelada() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Hamburguer", "Carne", 30.0, 15));

        Pedido pedido = pedidoService.realizarPedido(
                passageiro.getId(),
                restaurante.getId(),
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.25, -35.95),
                FormaPagamento.DINHEIRO,
                itens,
                null
        );

        pedidoService.cancelarPedido(pedido, passageiro.getId());

        Pedido atualizado = pedidoRepository.buscarPorId(pedido.getIdPedido());
        assertEquals(StatusCorrida.CANCELADA, atualizado.getStatusPedido());
    }

    @Test
    void aceitarPedido_deveMudarStatusParaEmCurso() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Sushi", "Peixe", 80.0, 25));

        Pedido pedido = pedidoService.realizarPedido(
                passageiro.getId(),
                restaurante.getId(),
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.3, -36.0),
                FormaPagamento.CARTAO,
                itens,
                null
        );

        boolean aceito = pedidoService.aceitarPedido(entregador, pedido);
        assertTrue(aceito);

        Pedido atualizado = pedidoRepository.buscarPorId(pedido.getIdPedido());
        assertEquals(StatusCorrida.EM_CURSO, atualizado.getStatusPedido());
        assertEquals(entregador.getId(), atualizado.getIdEntregador());
    }

    @Test
    void finalizarCorrida_deveAlterarStatusParaFinalizada() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Lasanha", "Frango", 40.0, 20));

        Pedido pedido = pedidoService.realizarPedido(
                passageiro.getId(),
                restaurante.getId(),
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.3, -36.0),
                FormaPagamento.CARTAO,
                itens,
                null
        );

        pedidoService.aceitarPedido(entregador, pedido);
        pedidoService.finalizarCorrida(pedido);

        Pedido atualizado = pedidoRepository.buscarPorId(pedido.getIdPedido());
        assertEquals(StatusCorrida.FINALIZADA, atualizado.getStatusPedido());
        assertNotNull(atualizado.getHoraFim());
    }

    @Test
    void calcularPrecoEstimado_deveRetornarValorPositivo() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Prato", "Arroz", 20.0, 10));

        double valor = pedidoService.calcularPrecoEstimado(
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.25, -35.95),
                itens
        );

        assertTrue(valor > 20.0);
    }

    @Test
    void calcularTempoDeEntrega_deveRetornarTempoMaiorQuePreparo() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Prato", "Arroz", 20.0, 30));

        int tempo = pedidoService.calcularTempoDeEntrega(
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.3, -36.0),
                itens
        );

        assertTrue(tempo >= 30);
    }

    @Test
    void verificarEntregadoresDisponiveis_deveRetornarTrue() {
        assertTrue(pedidoService.verificarEntregadoresDisponiveis());
    }
// .
    @Test
    void buscarPedidoAstivoPorEntregador_deveRetornarPedidoEmCurso() {

        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Taco", "Carne", 25.0, 10));

        Pedido pedido = pedidoService.realizarPedido(
                passageiro.getId(),
                restaurante.getId(),
                new Localizacao(-7.2, -35.9),
                new Localizacao(-7.3, -36.0),
                FormaPagamento.CARTAO,
                itens,
                null
        );

        pedidoService.aceitarPedido(entregador, pedido);

        Pedido ativo = pedidoService.buscarPedidoAtivoPorEntregador(entregador);
        assertNotNull(ativo);
        assertEquals(StatusCorrida.EM_CURSO, ativo.getStatusPedido());
    }
}
// test
