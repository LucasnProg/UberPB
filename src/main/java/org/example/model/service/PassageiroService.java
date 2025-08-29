package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;
import org.example.util.CrudUserError;

import java.util.List;

public class PassageiroService {

    private PassageiroRepository passageiros;

    public PassageiroService(PassageiroRepository repository) {
        this.passageiros = repository;
    }

    public void criarPassageiro(String nome, String email, String senha, String cpf, String telefone) {
        if (passageiros.existeCpf(cpf)) {
            throw new CrudUserError("Passageiro já cadastrado.");
        }

        Passageiro passageiro = new Passageiro(nome, email, senha, cpf, telefone);

        passageiros.salvarPassageiro(passageiro);
    }

    public List<Passageiro> listarPassageiros() {
        return passageiros.getPassageiros();
    }

    public Passageiro getPassageiro(String cpf){
        if (!passageiros.existeCpf(cpf)) {
            throw new CrudUserError("Passageiro não cadastrado no sistema.");
        }

        return passageiros.buscarPorCpf(cpf);
    }



    public void deletarPassageiros(String cpf) {
        if (!passageiros.existeCpf(cpf)) {
            throw new CrudUserError("Passageiro não cadastrado no sistema.");
        }

        passageiros.remover(cpf);
    }
}
