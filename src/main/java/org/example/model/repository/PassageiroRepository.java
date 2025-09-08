package org.example.model.repository;

import org.example.model.entity.Passageiro;

import java.util.ArrayList;
import java.util.List;

public class PassageiroRepository implements UsuarioRepository {


    // Lista em memória para testes
    private static JsonRepository<Passageiro> passageiroDB =
            new JsonRepository<>("src/main/resources/data/passageiros.json", Passageiro.class);
    private static List<Passageiro> passageirosCarregados = passageiroDB.carregar();

    public void salvarPassageiro(Passageiro passageiro) {
        atualizarPassageirosCarregados();
        int currentId = passageirosCarregados.size() + 1;
        passageiro.setId(currentId);
        passageirosCarregados.add(passageiro);
        passageiroDB.salvar(passageirosCarregados);
    }

    @Override
    public Passageiro buscarPorCpf(String cpfBusca) {
        for (Passageiro p : passageirosCarregados) {
            if (p.getCpf().equals(cpfBusca)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Passageiro buscarPorEmail(String email) {
        for (Passageiro p : passageirosCarregados) {
            if (p.getEmail().equals(email)) {
                return p;
            }
        }
        return null;
    }

    public Passageiro buscarPorId(int id) {
        for (Passageiro p : passageirosCarregados) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Passageiro> getPassageiros() {
        return passageirosCarregados;
    }

    public boolean existeCpf(String cpfBusca) {
        return passageirosCarregados.stream()
                .anyMatch(p -> p.getCpf().equals(cpfBusca));
    }

    @Override
    public boolean verificarEmail(String email) {
        return passageirosCarregados.stream()
                .anyMatch(p -> p.getEmail().equals(email));
    }

    public boolean realizarLogin(String email, String senha) {
        return passageirosCarregados.stream()
                .anyMatch(p -> p.getEmail().equals(email) && p.getSenha().equals(senha));
    }

    public static int getIdByCpf(String cpf) {
        return passageirosCarregados.stream()
                .filter(p -> p.getCpf().equals(cpf))
                .map(Passageiro::getId)
                .findFirst()
                .orElse(0);
    }

    public static void atualizarPassageirosCarregados() {
        passageirosCarregados = passageiroDB.carregar();
    }

    public static void limparTodos() {
        passageirosCarregados = new ArrayList<>();
        passageiroDB.salvar(passageirosCarregados);
    }

    @Override
    public void remover(String cpf) {
        atualizarPassageirosCarregados();
        passageirosCarregados.removeIf(p -> p.getCpf().equals(cpf));
        passageiroDB.salvar(passageirosCarregados);
    }
}
