package org.example.model.service;

import org.example.model.entity.Gerente;
import org.example.model.repository.GerenteRepository;
import org.example.util.CrudUserError;
import org.example.util.UsuarioNaoCadastrado;

import java.util.List;

public class GerenteService implements UsuarioService{

    private final GerenteRepository gerentes = new GerenteRepository();

    public GerenteService() {
    }

    public void criar(String nome, String email, String senha, String cpf, String telefone) {
        try{
            if (gerentes.existeCpf(cpf)) {
                throw new CrudUserError("Cpf como Gerente já cadastrado.");
            } else if (gerentes.verificarEmail(email)){
                throw new CrudUserError("Email como Gerente já cadastrado.");
            }

            Gerente gerente = new Gerente(nome, email, senha, cpf, telefone);

            gerentes.salvarGerente(gerente);
        } catch (CrudUserError e){
            System.out.println(e.getMessage());
        }
    }

    public List<Gerente> listar() {
        return gerentes.getGerentes();
    }

    public Gerente getGerentes(String cpf){
        try {
            if (!gerentes.existeCpf(cpf)) {
                throw new CrudUserError("Gerente não cadastrado no sistema.");
            }

            return gerentes.buscarPorCpf(cpf);
        } catch (CrudUserError e) {
            System.out.println(e.getMessage());
            return null;
        }
    }



    public void deletar(String cpf) {
        try {
            if (!gerentes.existeCpf(cpf)) {
                throw new CrudUserError("Gerente não cadastrado no sistema.");
            }

            gerentes.remover(cpf);
        } catch (CrudUserError e){
            System.out.println(e.getMessage());
        }
    }

    public boolean login(String email, String senha) {
        try {
            if (!gerentes.verificarEmail(email)) {
                throw new UsuarioNaoCadastrado("Esse email não está cadastrado como gerente.");
            }
            return gerentes.realizarLogin(email,senha);
        } catch (UsuarioNaoCadastrado e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
