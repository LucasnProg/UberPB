package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Passageiros.
 */
public class PassageiroService {

    private final PassageiroRepository passageiroRepository = new PassageiroRepository();

    /**
     * Tenta autenticar um passageiro com base no email e senha.
     * @param email O email do passageiro.
     * @param senha A senha do passageiro.
     * @return O objeto Passageiro se a autenticação for bem-sucedida, caso contrário null.
     */
    public Passageiro login(String email, String senha) {
        Passageiro passageiro = passageiroRepository.buscarPorEmail(email);
        if (passageiro != null && passageiro.getSenha().equals(senha)) {
            return passageiro;
        }
        // A View será responsável por exibir a mensagem de erro genérica.
        return null;
    }

    /**
     * Cria um novo passageiro no sistema após validar se o email ou CPF já existem.
     * @return O objeto Passageiro recém-criado, ou null se o cadastro falhar.
     */
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
        return novoPassageiro;
    }

    /**
     * Busca um passageiro pelo seu ID.
     * @param id O ID do passageiro.
     * @return O objeto Passageiro encontrado, ou null.
     */
    public Passageiro buscarPorId(int id) {
        return passageiroRepository.buscarPorId(id);
    }
}