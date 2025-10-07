package org.example.model.service;

import org.example.model.entity.Gerente;
import org.example.model.repository.GerenteRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Gerentes.
 */
public class GerenteService {

    private final GerenteRepository gerenteRepository = new GerenteRepository();

    /**
     * Tenta autenticar um gerente com base no email e senha.
     */
    public Gerente login(String email, String senha) {
        Gerente gerente = gerenteRepository.buscarPorEmail(email);
        if (gerente != null && gerente.getSenha().equals(senha)) {
            return gerente;
        }
        return null;
    }

    /**
     * Cria um novo gerente no sistema, evitando duplicidade de email.
     */
    public Gerente criar(String nome, String email, String senha, String cpf, String telefone) {
        if (gerenteRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }
        Gerente novoGerente = new Gerente(nome, email, senha, cpf, telefone);
        gerenteRepository.salvar(novoGerente);
        return novoGerente;
    }


}
