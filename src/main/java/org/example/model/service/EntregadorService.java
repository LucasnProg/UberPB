package org.example.model.service;

import org.example.model.entity.Entregador;
import org.example.model.repository.EntregadorRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Entregadores.
 */
public class EntregadorService {

    private final EntregadorRepository entregadorRepository = new EntregadorRepository();

    /**
     * Realiza o login do entregador.
     * 
     * @param email Email do entregador.
     * @param senha Senha do entregador.
     * @return O entregador logado ou null se falhar.
     */
    public Entregador login(String email, String senha) {
        Entregador entregador = entregadorRepository.buscarPorEmail(email);
        if (entregador != null && entregador.getSenha().equals(senha)) {
            return entregador;
        }
        System.out.println("\n[ERRO] E-mail ou senha inválidos.");
        return null;
    }

    /**
     * Cadastra um novo entregador no sistema.
     * 
     * @param nome     Nome do entregador.
     * @param email    Email.
     * @param senha    Senha.
     * @param cpf      CPF.
     * @param telefone Telefone.
     * @return O entregador cadastrado ou null se falhar validação.
     */
    public Entregador criar(String nome, String email, String senha, String cpf, String telefone) {
        if (entregadorRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }
        if (entregadorRepository.buscarPorCpf(cpf) != null) {
            System.out.println("\n[ERRO] O CPF informado já está cadastrado.");
            return null;
        }
        Entregador novoEntregador = new Entregador(nome, email, senha, cpf, telefone);
        entregadorRepository.salvar(novoEntregador);
        System.out.println("\nEntregador cadastrado com sucesso!");
        return novoEntregador;
    }

    public Entregador buscarPorId(int id) {
        return entregadorRepository.buscarPorId(id);
    }

    public void atualizar(Entregador entregador) {
        entregadorRepository.atualizar(entregador);
    }
}
