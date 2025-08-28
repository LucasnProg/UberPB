package org.example.model.repository;

import com.google.gson.Gson;
import org.example.model.entity.Passageiro;
import org.example.model.entity.Usuario;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PassageirosRepository implements UsuarioRepository{

    private JsonRepository<Passageiro> passageirosDB = new JsonRepository<Passageiro>("src/main/resources/data/passageiros.json", Passageiro.class);
    private List<Passageiro> passageiros = passageirosDB.carregar();

    public void salvarPassageiro(Usuario passageiro) {
        passageiros = passageirosDB.carregar();
        int currentId = passageiros.size()+1;
        passageiro.setId(currentId);
        passageiros.add((Passageiro) passageiro);
        passageirosDB.salvar(passageiros);
    }


    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void criarUsuario(String nome, String email, String cpf, String telefone) {

    }

    @Override
    public List<Usuario> listarTodos() {
        return List.of();
    }

    @Override
    public void remover(String id) {

    }

    @Override
    public void salvarEntidade(Usuario usuario) {

    }
}
