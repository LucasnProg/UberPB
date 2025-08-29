package org.example.model.service;

import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.repository.MotoristaRepository;
import org.example.util.CrudUserError;

import java.util.List;

public class MotoristaService {

    private MotoristaRepository motoristas;

    public MotoristaService(MotoristaRepository repository) {
        this.motoristas = repository;
    }

    public void criarMotorista(String nome, String email, String senha, String cpf, String telefone) {
        if (motoristas.existeCpf(cpf)) {
            throw new CrudUserError("Motorista já cadastrado.");
        }

        Motorista motorista = new Motorista(nome, email, senha, cpf, telefone);

        motoristas.salvarMotorista(motorista);
    }

    public List<Motorista> listarMotoristas() {
        return motoristas.getMotoristas();
    }

    public Motorista getMotorista(String cpf){
        if (!motoristas.existeCpf(cpf)) {
            throw new CrudUserError("Motorista não cadastrado no sistema.");
        }

        return motoristas.buscarPorCpf(cpf);
    }



    public void deletarMotorista(String cpf) {
        if (!motoristas.existeCpf(cpf)) {
            throw new CrudUserError("Motorista não cadastrado no sistema.");
        }

        motoristas.remover(cpf);
    }
}
