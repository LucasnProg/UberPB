package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    public void cadastrar(String nome, String email, String senha, String cpf, String telefone);
    public void deletar(String cpf);
    public boolean login(String email, String senha);
}
