package org.example.model.repository;

import org.example.model.entity.Motorista;

import java.util.ArrayList;
import java.util.List;

public class MotoristaRepository implements UsuarioRepository {

    private static JsonRepository<Motorista> motoristasDB =
            new JsonRepository<>("src/main/resources/data/motoristas.json", Motorista.class);

    private static List<Motorista> motoristasCarregados = motoristasDB.carregar();

    public void salvarMotorista(Motorista motorista) {
        atualizarMotoristasCarregados();
        int currentId = motoristasCarregados.size() + 1;
        motorista.setId(currentId);
        motoristasCarregados.add(motorista);
        motoristasDB.salvar(motoristasCarregados);
    }

    @Override
    public Motorista buscarPorCpf(String cpfBusca) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getCpf().equals(cpfBusca)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public Motorista buscarPorEmail(String email) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getEmail().equals(email)) {
                return m;
            }
        }
        return null;
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

    public static void atualizarMotoristasCarregados() {
        motoristasCarregados = motoristasDB.carregar();
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

    public static int getIdByCpf(String cpf) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados) {
            if (m.getCpf().equals(cpf)) {
                return m.getId();
            }
        }
        return 0;
    }

    // Método extra para testes: limpa todos os motoristas
    public static void limparTodos() {
        motoristasCarregados = new ArrayList<>();
        motoristasDB.salvar(motoristasCarregados);
    }
}
