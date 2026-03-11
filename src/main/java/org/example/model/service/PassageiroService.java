package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.PassageiroRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Passageiros.
 */
public class PassageiroService {

    private static final PassageiroRepository passageiroRepository = new PassageiroRepository();

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

    public static Passageiro buscarPorId(int id) {
        return passageiroRepository.buscarPorId(id);
    }

    public static void atualizar(Passageiro passageiro) {
        passageiroRepository.atualizar(passageiro);
    }

    public static Localizacao getLocalizacaoPorId(int idCliente){
        PassageiroService ps = new PassageiroService();
        Passageiro passageiro = ps.buscarPorId(idCliente);

        return passageiro.getLocalCasa();
    }

    public static void atualizarPedidoNotificado(Pedido pedido) {
        Passageiro cliente = passageiroRepository.buscarPorId(pedido.getIdCliente());
        for (Pedido p : cliente.getPedidosPendentes()){
            if (p.getIdPedido() == pedido.getIdPedido()){
                p.setAceiteRestaurante(true);
                p.setStatusPedido(StatusCorrida.EM_PREPARO);
            }
        }
        passageiroRepository.atualizar(cliente);
    }

    public static void receberAvaliacao(int idPassageiro, Double avaliacao){
        Passageiro passageiroAvaliado = buscarPorId(idPassageiro);

        if (passageiroAvaliado.getAvaliacao() == null){
            passageiroAvaliado.setAvaliacao(avaliacao);
        } else {
            Double novaAvaliacao = (passageiroAvaliado.getAvaliacao() + avaliacao)/2;
            passageiroAvaliado.setAvaliacao(novaAvaliacao);
        }

        atualizar(passageiroAvaliado);
    }


}