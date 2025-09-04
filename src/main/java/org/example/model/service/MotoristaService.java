package org.example.model.service;

import org.example.model.entity.Motorista;
import org.example.model.repository.MotoristaRepository;
import org.example.util.CrudUserError;
import org.example.util.UsuarioNaoCadastrado;

import java.util.List;

public class MotoristaService implements UsuarioService{

    private final MotoristaRepository motoristas = new MotoristaRepository();

    public MotoristaService() {
    }

    @Override
    public void criar(String nome, String email, String senha, String cpf, String telefone) {
        try{
            if (motoristas.existeCpf(cpf)) {
                throw new CrudUserError("Cpf como Motorista já cadastrado.");
            } else if(motoristas.verificarEmail(email)){
                throw new CrudUserError("Email como Motorista já cadastrado.");
            }

            Motorista motorista = new Motorista(nome, email, senha, cpf, telefone);

            motoristas.salvarMotorista(motorista);
        } catch (CrudUserError e){
            System.out.println(e.getMessage());
        }
    }

    public List<Motorista> listar() {
        return motoristas.getMotoristas();
    }

    public Motorista getMotorista(String cpf){
        try{
            if (!motoristas.existeCpf(cpf)) {
                throw new CrudUserError("Motorista não cadastrado no sistema.");
            }

            return motoristas.buscarPorCpf(cpf);
        } catch (CrudUserError e) {
            System.out.println(e.getMessage());
            return null;
        }

    }


    @Override
    public void deletar(String cpf) {
        try {
            if (!motoristas.existeCpf(cpf)) {
                throw new CrudUserError("Motorista não cadastrado no sistema.");
            }

            motoristas.remover(cpf);
        } catch (CrudUserError e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public boolean login(String email, String senha) {
        try {
            if (!motoristas.verificarEmail(email)) {
                throw new UsuarioNaoCadastrado("Esse email não está cadastrado como motorista.");
            }
            return motoristas.realizarLogin(email,senha);
        } catch (UsuarioNaoCadastrado e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
