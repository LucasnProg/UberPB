package org.example.model.repository;

import org.example.model.entity.Passageiro;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Passageiro.
 */
public class PassageiroRepository {

    private final JsonRepository<Passageiro> passageiroDB;

    public PassageiroRepository() {
        this.passageiroDB = new JsonRepository<>("src/main/resources/data/passageiros.json", Passageiro.class);
    }

    public void salvar(Passageiro passageiro) {
        List<Passageiro> passageiros = passageiroDB.carregar();
        int proximoId = passageiros.stream().mapToInt(Passageiro::getId).max().orElse(0) + 1;
        passageiro.setId(proximoId);
        passageiros.add(passageiro);
        passageiroDB.salvar(passageiros);
    }

    public void atualizar(Passageiro passageiroAtualizado) {
        List<Passageiro> passageiros = passageiroDB.carregar();
        for (int i = 0; i < passageiros.size(); i++) {
            if (passageiros.get(i).getId() == passageiroAtualizado.getId()) {
                passageiros.set(i, passageiroAtualizado);
                passageiroDB.salvar(passageiros);
                return;
            }
        }
    }

    public Passageiro buscarPorId(int id) {
        return passageiroDB.carregar().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public Passageiro buscarPorEmail(String email) {
        return passageiroDB.carregar().stream().filter(p -> p.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    public Passageiro buscarPorCpf(String cpf) {
        return passageiroDB.carregar().stream().filter(p -> p.getCpf().equals(cpf)).findFirst().orElse(null);
    }

    // NOVO: limpa todos os registros
    public void limpar() {
        passageiroDB.salvar(new ArrayList<>());
    }
}
