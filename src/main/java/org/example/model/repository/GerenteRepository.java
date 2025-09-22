package org.example.model.repository;

import org.example.model.entity.Gerente;
import java.util.List;

/**
 * Repositório para gerenciar gerentes com persistência via JSON.
 */
public class GerenteRepository {

    private final JsonRepository<Gerente> gerentesDB;

    public GerenteRepository() {
        this.gerentesDB = new JsonRepository<>("src/main/resources/data/gerentes.json", Gerente.class);
    }

    public void salvar(Gerente gerente) {
        List<Gerente> gerentes = gerentesDB.carregar();
        int proximoId = gerentes.stream().mapToInt(Gerente::getId).max().orElse(0) + 1;
        gerente.setId(proximoId);
        gerentes.add(gerente);
        gerentesDB.salvar(gerentes);
    }

    public Gerente buscarPorEmail(String email) {
        return gerentesDB.carregar().stream()
                .filter(g -> g.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    public Gerente buscarPorCpf(String cpf) {
        return gerentesDB.carregar().stream()
                .filter(g -> g.getCpf().equals(cpf))
                .findFirst().orElse(null);
    }

    public void limpar() {
        gerentesDB.salvar(List.of());
    }

    public List<Gerente> getGerentes() {
        return gerentesDB.carregar();
    }
}
