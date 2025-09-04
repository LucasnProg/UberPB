package org.example.model.repository;

import org.example.model.entity.Usuario;

public interface UsuarioRepository {
    public Usuario buscarPorCpf(String cpf);
    public Usuario buscarPorEmail(String email);
    public void remover(String id);
    public boolean verificarEmail(String email);
    public boolean realizarLogin(String email, String senha);

}
