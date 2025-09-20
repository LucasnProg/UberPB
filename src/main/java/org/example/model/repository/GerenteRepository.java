package org.example.model.repository;

import org.example.model.entity.Gerente;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Gerente.
 */
public class GerenteRepository {

    private final JsonRepository<Gerente> gerentesDB;

    public GerenteRepository() {
        this.gerentesDB = new JsonRepository<>("src/main/resources/data/gerentes.json", Gerente.class);
    }

    /**
     * Salva um novo gerente, gerando um ID único e robusto.
     * @param gerente O novo gerente a ser salvo.
     */
    public void salvar(Gerente gerente) {
        List<Gerente> gerentes = gerentesDB.carregar();
        int proximoId = gerentes.stream().mapToInt(Gerente::getId).max().orElse(0) + 1;
        gerente.setId(proximoId);
        gerentes.add(gerente);
        gerentesDB.salvar(gerentes);
    }

    /**
     * Busca um gerente pelo seu email.
     * @param email O email a ser buscado.
     * @return O objeto Gerente encontrado ou null.
     */
    public Gerente buscarPorEmail(String email) {
        return gerentesDB.carregar().stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um gerente pelo seu CPF.
     * @param cpf O CPF a ser buscado.
     * @return O objeto Gerente encontrado ou null.
     */
    public Gerente buscarPorCpf(String cpf) {
        return gerentesDB.carregar().stream()
                .filter(g -> g.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }
}