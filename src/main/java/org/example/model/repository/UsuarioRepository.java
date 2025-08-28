package org.example.model.repository;

import org.example.model.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> buscarPorEmail(String email);
    void criarUsuario(String nome, String email, String cpf, String telefone);
    List<Usuario> listarTodos();
    void remover(String id);
    void salvarEntidade(Usuario usuario);
}
