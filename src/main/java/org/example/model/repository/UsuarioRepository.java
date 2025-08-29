package org.example.model.repository;

import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario buscarPorCpf(String cpf);
    void remover(String id);
}
