package org.example.model.repository;

import org.example.model.entity.Entregador;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Entregador.
 */
public class EntregadorRepository {

    private final JsonRepository<Entregador> entregadorDB;

    public EntregadorRepository() {
        this.entregadorDB = new JsonRepository<>("src/main/resources/data/entregadores.json", Entregador.class);
    }

    /**
     * Salva um novo entregador no banco de dados.
     * Gera um novo ID automaticamente.
     * 
     * @param entregador O entregador a ser salvo.
     */
    public void salvar(Entregador entregador) {
        List<Entregador> entregadores = entregadorDB.carregar();
        int proximoId = entregadores.stream().mapToInt(Entregador::getId).max().orElse(0) + 1;
        entregador.setId(proximoId);
        entregadores.add(entregador);
        entregadorDB.salvar(entregadores);
    }

    /**
     * Atualiza os dados de um entregador existente.
     * 
     * @param entregadorAtualizado O entregador com os dados atualizados.
     */
    public void atualizar(Entregador entregadorAtualizado) {
        List<Entregador> entregadores = entregadorDB.carregar();
        for (int i = 0; i < entregadores.size(); i++) {
            if (entregadores.get(i).getId() == entregadorAtualizado.getId()) {
                entregadores.set(i, entregadorAtualizado);
                entregadorDB.salvar(entregadores);
                return;
            }
        }
    }

    /**
     * Busca um entregador pelo seu ID.
     * 
     * @param id O ID do entregador.
     * @return O entregador encontrado ou null se não existir.
     */
    public Entregador buscarPorId(int id) {
        return entregadorDB.carregar().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um entregador pelo CPF.
     * 
     * @param cpf O CPF do entregador.
     * @return O entregador encontrado ou null se não existir.
     */
    public Entregador buscarPorCpf(String cpf) {
        return entregadorDB.carregar().stream()
                .filter(e -> e.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um entregador pelo Email.
     * 
     * @param email O email do entregador.
     * @return O entregador encontrado ou null se não existir.
     */
    public Entregador buscarPorEmail(String email) {
        return entregadorDB.carregar().stream()
                .filter(e -> e.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<Entregador> getEntregadores(){ return (ArrayList<Entregador>) entregadorDB.carregar();}
}
