package org.example.model.repository;

import org.example.model.entity.Motorista;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Motorista.
 */
public class MotoristaRepository {

    private final JsonRepository<Motorista> motoristasDB;

    public MotoristaRepository() {
        this.motoristasDB = new JsonRepository<>("src/main/resources/data/motoristas.json", Motorista.class);
    }

    /**
     * Salva um novo motorista, gerando ID automaticamente.
     */
    public void salvar(Motorista motorista) {
        List<Motorista> motoristas = motoristasDB.carregar();
        int proximoId = motoristas.stream().mapToInt(Motorista::getId).max().orElse(0) + 1;
        motorista.setId(proximoId);
        motoristas.add(motorista);
        motoristasDB.salvar(motoristas);
    }

    /**
     * Atualiza um motorista existente com base no ID.
     */
    public void atualizar(Motorista motoristaAtualizado) {
        List<Motorista> motoristas = motoristasDB.carregar();
        for (int i = 0; i < motoristas.size(); i++) {
            if (motoristas.get(i).getId() == motoristaAtualizado.getId()) {
                motoristas.set(i, motoristaAtualizado);
                motoristasDB.salvar(motoristas);
                return;
            }
        }
    }

    public Motorista buscarPorId(int id) {
        return motoristasDB.carregar().stream()
                .filter(m -> m.getId() == id)
                .findFirst().orElse(null);
    }

    public Motorista buscarPorEmail(String email) {
        return motoristasDB.carregar().stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    public List<Motorista> getMotoristas() {
        return motoristasDB.carregar();
    }

    /**
     * Método para limpar todos os motoristas (útil para testes).
     */
    public void limpar() {
        motoristasDB.salvar(List.of());
    }
}
