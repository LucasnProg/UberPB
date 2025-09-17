package org.example.model.repository;

import org.example.model.entity.Motorista;

import java.util.ArrayList;
import java.util.List;

public class MotoristaRepository implements UsuarioRepository {

    private final JsonRepository<Motorista> motoristasDB;

    private List<Motorista> motoristasCarregados;

    public MotoristaRepository() {
        this.motoristasDB = new JsonRepository<>("src/main/resources/data/motoristas.json", Motorista.class);
        this.motoristasCarregados = this.motoristasDB.carregar();
    }
    /**
     * Salva um novo motorista, gerando um ID robusto.
     * @param motorista O novo motorista a ser salvo.
     */
    public void salvar(Motorista motorista) {
        atualizarMotoristasCarregados();
        int proximoId = motoristasCarregados.stream()
                .mapToInt(Motorista::getId)
                .max()
                .orElse(0) + 1;
        motorista.setId(proximoId);
        motoristasCarregados.add(motorista);
        motoristasDB.salvar(motoristasCarregados);
    }

    /**
     * Atualiza os dados de um motorista existente.
     * @param motoristaAtualizado O motorista com os dados atualizados.
     */
    public void atualizar(Motorista motoristaAtualizado) {
        atualizarMotoristasCarregados();
        for (int i = 0; i < motoristasCarregados.size(); i++) {
            if (motoristasCarregados.get(i).getId() == motoristaAtualizado.getId()) {
                motoristasCarregados.set(i, motoristaAtualizado);
                motoristasDB.salvar(motoristasCarregados);
                return;
            }
        }
    }

    @Override
    public Motorista buscarPorCpf(String cpfBusca) {
        atualizarMotoristasCarregados();
        return motoristasCarregados.stream().filter(m -> m.getCpf().equals(cpfBusca)).findFirst().orElse(null);
    }

    @Override
    public Motorista buscarPorEmail(String email) {
        atualizarMotoristasCarregados();
        return motoristasCarregados.stream().filter(m -> m.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public void remover(String cpf) {
        atualizarMotoristasCarregados();
        motoristasCarregados.removeIf(m -> m.getCpf().equals(cpf));
        motoristasDB.salvar(motoristasCarregados);
    }

    public List<Motorista> getMotoristas() {
        atualizarMotoristasCarregados();
        return motoristasCarregados;
    }

    public boolean existeCpf(String cpfBusca) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getCpf().equals(cpfBusca)) {
                return true;
            }
        }
        return false;
    }

    public void atualizarMotoristasCarregados() {
        this.motoristasCarregados = motoristasDB.carregar();
    }

    @Override
    public boolean verificarEmail(String email) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean realizarLogin(String email, String senha) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getEmail().equals(email) && m.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    public int getIdByCpf(String cpf) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getCpf().equals(cpf)) {
                return m.getId();
            }
        }
        return 0;
    }

    // Método extra para testes: limpa todos os motoristas
    public void limparTodos() {
        motoristasCarregados = new ArrayList<>();
        motoristasDB.salvar(motoristasCarregados);
    }

    public Motorista buscarPorId(int motoristaId) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getId() == motoristaId) {
                return m;
            }
        }
        return null;
    }

    public void atualizarMotorista(Motorista motoristaAtualizado) {
        // Garante que a lista em memória está sincronizada com o arquivo JSON
        atualizarMotoristasCarregados();

        // Itera pela lista para encontrar o motorista com o mesmo ID
        for (int i = 0; i < motoristasCarregados.size(); i++) {
            if (motoristasCarregados.get(i).getId() == motoristaAtualizado.getId()) {

                // Substitui o objeto antigo pelo novo na posição encontrada
                motoristasCarregados.set(i, motoristaAtualizado);

                // Salva a lista inteira (agora atualizada) de volta no arquivo JSON
                motoristasDB.salvar(motoristasCarregados);

                // Sai do método, pois a atualização foi concluída
                return;
            }
        }
    }

    public List<Motorista> getMotoristasCarregados() {
        atualizarMotoristasCarregados();
        return motoristasCarregados;
    }
}
