package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Passageiros.
 */
public class PassageiroService {

    private final PassageiroRepository passageiroRepository = new PassageiroRepository();

    public Passageiro login(String email, String senha) {
        Passageiro passageiro = passageiroRepository.buscarPorEmail(email);
        if (passageiro != null && passageiro.getSenha().equals(senha)) {
            return passageiro;
        }
        System.out.println("\n[ERRO] E-mail ou senha inválidos.");
        return null;
    }

    public Passageiro criar(String nome, String email, String senha, String cpf, String telefone) {
        if (passageiroRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }
        if (passageiroRepository.buscarPorCpf(cpf) != null) {
            System.out.println("\n[ERRO] O CPF informado já está cadastrado.");
            return null;
        }
        Passageiro novoPassageiro = new Passageiro(nome, email, senha, cpf, telefone);
        passageiroRepository.salvar(novoPassageiro);
        System.out.println("\nPassageiro cadastrado com sucesso!");
        return passageiroRepository.buscarPorEmail(email);
    }

    public Passageiro buscarPorId(int id) {
        return passageiroRepository.buscarPorId(id);
    }

    public void atualizar(Passageiro passageiro) {
        passageiroRepository.atualizar(passageiro);
    }
}