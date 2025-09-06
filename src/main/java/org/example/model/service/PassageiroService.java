package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;
import org.example.util.CrudUserError;
import org.example.util.LoginInvalido;
import org.example.util.UsuarioNaoCadastrado;

import java.util.List;

public class PassageiroService implements UsuarioService {

    private final PassageiroRepository passageiros = new PassageiroRepository();
    private Passageiro passageiroLogado;

    public PassageiroService() {
    }

    public Passageiro getPassageiroLogado() {
        return passageiroLogado;
    }

    public void setPassageiroLogado(Passageiro passageiroLogado) {
        this.passageiroLogado = passageiroLogado;
    }

    @Override
    public void criar(String nome, String email, String senha, String cpf, String telefone) {
        try {
            if (passageiros.existeCpf(cpf)) {
                throw new CrudUserError("CPF como Passageiro já cadastrado.");
            } else if (passageiros.verificarEmail(email)) {
                throw new CrudUserError("Email como Passageiro já cadastrado.");
            }

            Passageiro passageiro = new Passageiro(nome, email, senha, cpf, telefone);
            passageiros.salvarPassageiro(passageiro);
        } catch (CrudUserError e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Passageiro> listar() {
        return passageiros.getPassageiros();
    }

    public Passageiro getPassageiro(String cpf) {
        try {
            if (!passageiros.existeCpf(cpf)) {
                throw new CrudUserError("Passageiro não cadastrado no sistema.");
            }
            return passageiros.buscarPorCpf(cpf);
        } catch (CrudUserError e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void deletar(String cpf) {
        try {
            if (!passageiros.existeCpf(cpf)) {
                throw new CrudUserError("Passageiro não cadastrado no sistema.");
            }
            passageiros.remover(cpf);
        } catch (CrudUserError e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean login(String email, String senha) {
        try {
            if (!passageiros.verificarEmail(email)) {
                throw new UsuarioNaoCadastrado("Esse email não está cadastrado como passageiro.");
            }
            if (passageiros.realizarLogin(email, senha)) {
                setPassageiroLogado(passageiros.buscarPorEmail(email));
                return true;
            } else {
                throw new LoginInvalido("Senha incorreta.\nVerifique a senha e tente novamente.");
            }
        } catch (UsuarioNaoCadastrado | LoginInvalido e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
