package org.example.model.repository;

import org.example.model.entity.Avaliacao;
import org.example.model.entity.TipoAvaliacaoContexto;
import org.example.model.entity.TipoParteAvaliacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Repositório para gerenciar a persistência de entidades Avaliacao.
 *
 * Persistência em JSON em: src/main/resources/data/avaliacoes.json
 */
public class AvaliacaoRepository {

    private final JsonRepository<Avaliacao> avaliacoesDB;

    public AvaliacaoRepository() {
        this.avaliacoesDB = new JsonRepository<>("src/main/resources/data/avaliacoes.json", Avaliacao.class);
    }

    /**
     * Salva uma nova avaliação, gerando ID automaticamente.
     *
     * Regras de sanidade:
     * - nota deve estar entre 1 e 5
     * - contexto CORRIDA exige corridaId preenchido; contexto PEDIDO exige pedidoId preenchido
     */
    public void salvar(Avaliacao avaliacao) {
        validarBasico(avaliacao);

        List<Avaliacao> avaliacoes = avaliacoesDB.carregar();
        int proximoId = avaliacoes.stream().mapToInt(Avaliacao::getId).max().orElse(0) + 1;
        avaliacao.setId(proximoId);

        // garante timestamp
        if (avaliacao.getDataHora() == null) {
            avaliacao.setDataHora(LocalDateTime.now());
        }

        avaliacoes.add(avaliacao);
        avaliacoesDB.salvar(avaliacoes);
    }

    /**
     * Atualiza uma avaliação existente (match por ID).
     */
    public void atualizar(Avaliacao avaliacaoAtualizada) {
        validarBasico(avaliacaoAtualizada);
        List<Avaliacao> avaliacoes = avaliacoesDB.carregar();
        for (int i = 0; i < avaliacoes.size(); i++) {
            if (avaliacoes.get(i).getId() == avaliacaoAtualizada.getId()) {
                avaliacoes.set(i, avaliacaoAtualizada);
                avaliacoesDB.salvar(avaliacoes);
                return;
            }
        }
    }

    public Avaliacao buscarPorId(int id) {
        return avaliacoesDB.carregar().stream()
                .filter(a -> a.getId() == id)
                .findFirst().orElse(null);
    }

    public List<Avaliacao> listarTodas() {
        return avaliacoesDB.carregar();
    }

    /**
     * Lista avaliações recebidas por uma "parte" (ex: motoristaId).
     */
    public List<Avaliacao> listarPorAvaliado(TipoParteAvaliacao tipoAvaliado, int avaliadoId) {
        return avaliacoesDB.carregar().stream()
                .filter(a -> a.getTipoAvaliado() == tipoAvaliado && a.getAvaliadoId() == avaliadoId)
                .collect(Collectors.toList());
    }

    /**
     * Lista avaliações feitas por uma "parte".
     */
    public List<Avaliacao> listarPorAvaliador(TipoParteAvaliacao tipoAvaliador, int avaliadorId) {
        return avaliacoesDB.carregar().stream()
                .filter(a -> a.getTipoAvaliador() == tipoAvaliador && a.getAvaliadorId() == avaliadorId)
                .collect(Collectors.toList());
    }

    public List<Avaliacao> listarPorCorridaId(int corridaId) {
        return avaliacoesDB.carregar().stream()
                .filter(a -> a.getContexto() == TipoAvaliacaoContexto.CORRIDA)
                .filter(a -> Objects.equals(a.getCorridaId(), corridaId))
                .collect(Collectors.toList());
    }

    public List<Avaliacao> listarPorPedidoId(int pedidoId) {
        return avaliacoesDB.carregar().stream()
                .filter(a -> a.getContexto() == TipoAvaliacaoContexto.PEDIDO)
                .filter(a -> Objects.equals(a.getPedidoId(), pedidoId))
                .collect(Collectors.toList());
    }

    /**
     * Método para limpar todas as avaliações (útil para testes).
     */
    public void limpar() {
        avaliacoesDB.salvar(List.of());
    }

    private void validarBasico(Avaliacao a) {
        if (a == null) {
            throw new IllegalArgumentException("Avaliacao não pode ser nula");
        }
        if (a.getTipoAvaliador() == null || a.getTipoAvaliado() == null) {
            throw new IllegalArgumentException("Tipo do avaliador/avaliado é obrigatório");
        }
        if (a.getContexto() == null) {
            throw new IllegalArgumentException("Contexto da avaliação é obrigatório");
        }
        if (a.getNota() < 1 || a.getNota() > 5) {
            throw new IllegalArgumentException("Nota inválida: use 1..5");
        }
        if (a.getContexto() == TipoAvaliacaoContexto.CORRIDA) {
            if (a.getCorridaId() == null) {
                throw new IllegalArgumentException("Contexto CORRIDA exige corridaId");
            }
            if (a.getPedidoId() != null) {
                throw new IllegalArgumentException("Contexto CORRIDA não deve ter pedidoId");
            }
        }
        if (a.getContexto() == TipoAvaliacaoContexto.PEDIDO) {
            if (a.getPedidoId() == null) {
                throw new IllegalArgumentException("Contexto PEDIDO exige pedidoId");
            }
            if (a.getCorridaId() != null) {
                throw new IllegalArgumentException("Contexto PEDIDO não deve ter corridaId");
            }
        }
    }
}