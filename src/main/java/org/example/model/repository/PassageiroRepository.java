package org.example.model.repository;

import org.example.model.entity.Passageiro;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Passageiro.
 */
public class PassageiroRepository {

    private final JsonRepository<Passageiro> passageiroDB;

    public PassageiroRepository() {
        this.passageiroDB = new JsonRepository<>("src/main/resources/data/passageiros.json", Passageiro.class);
    }

    /**
     * Salva um novo passageiro, gerando um ID único e robusto.
     * @param passageiro O novo passageiro a ser salvo.
     */
    public void salvar(Passageiro passageiro) {
        List<Passageiro> passageiros = passageiroDB.carregar();
        int proximoId = passageiros.stream().mapToInt(Passageiro::getId).max().orElse(0) + 1;
        passageiro.setId(proximoId);
        passageiros.add(passageiro);
        passageiroDB.salvar(passageiros);
    }

    /**
     * Busca um passageiro pelo seu ID.
     * @param id O ID a ser buscado.
     * @return O objeto Passageiro encontrado ou null.
     */
    public Passageiro buscarPorId(int id) {
        return passageiroDB.carregar().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um passageiro pelo seu email.
     * @param email O email a ser buscado.
     * @return O objeto Passageiro encontrado ou null.
     */
    public Passageiro buscarPorEmail(String email) {
        return passageiroDB.carregar().stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um passageiro pelo seu CPF.
     * @param cpf O CPF a ser buscado.
     * @return O objeto Passageiro encontrado ou null.
     */
    public Passageiro buscarPorCpf(String cpf) {
        return passageiroDB.carregar().stream()
                .filter(p -> p.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }
}