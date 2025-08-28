package org.example.model.repository;

import org.example.model.entity.Usuario;

import java.util.List;

public interface Repository<T> {
    void salvar(List<T> entidades);

    List<T> carregar();
}
