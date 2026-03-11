package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.entity.MenuItem;
import org.example.model.repository.PedidoRepository;
import org.example.model.repository.RestauranteRepository;

import java.awt.*;
import java.util.ArrayList;

/**
 * Service responsável pela lógica de negócio relacionada a Restaurantes.
 */
public class RestauranteService {

    private static final RestauranteRepository restauranteRepository = new RestauranteRepository();
    private final PedidoRepository pedidoRepository = new PedidoRepository();

    public Restaurante login(String email, String senha) {
        Restaurante restaurante = restauranteRepository.buscarPorEmail(email);

        if (restaurante != null && restaurante.getSenha().equals(senha)) {
            return restaurante;
        }
        System.out.println("\n[ERRO] E-mail ou senha inválidos.");
        return null;
    }

    /**
     * Cadastra um novo restaurante no sistema.
     * 
     * @param nome     Nome do restaurante.
     * @param email    Email.
     * @param senha    Senha.
     * @param cnpj     CNPJ.
     * @param telefone Telefone.
     * @return O restaurante cadastrado ou null se falhar validação.
     */
    public Restaurante criar(String nome, String email, String senha, String cnpj, String telefone, String categoria,
            Localizacao localizacao) {
        if (restauranteRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }

        if (restauranteRepository.buscarPorCnpj(cnpj) != null) {
            System.out.println("\n[ERRO] O CNPJ informado já está cadastrado.");
            return null;
        }
        Restaurante novoRestaurante = new Restaurante(nome, email, senha, cnpj, telefone, categoria, localizacao);
        restauranteRepository.salvar(novoRestaurante);
        System.out.println("\nRestaurante cadastrado com sucesso!");
        return novoRestaurante;
    }

    public static Restaurante buscarPorId(int id) {
        return restauranteRepository.buscarPorId(id);
    }

    public static void atualizar(Restaurante restaurante) {
        restauranteRepository.atualizar(restaurante);
    }

    public ArrayList<Restaurante> listarRestaurantes() {
        return restauranteRepository.getAll();
    }

    public ArrayList<MenuItem> getMenu(int restauranteId) {
        Restaurante restaurante = restauranteRepository.buscarPorId(restauranteId);

        return restaurante.getMenu();
    }

    public static Localizacao getLocalizacaoPorID(int IdRestaurante) {
        RestauranteService rs = new RestauranteService();
        Restaurante restaurante = rs.buscarPorId(IdRestaurante);

        return restaurante.getEndereco();
    }

    public void adicionarItemAoCardapio(Restaurante restaurante, MenuItem item) {
        try {
            item.setId(restaurante.getMenu().size() + 1);
            restaurante.getMenu().add(item);
        } catch (NullPointerException e) {
            item.setId(1);
            ArrayList<MenuItem> menu = new ArrayList<>();
            menu.add(item);
            restaurante.setMenu(menu);
        }
        atualizar(restaurante);
    }

    public void removerItemPorId(Restaurante restaurante, int id) {
        restaurante.getMenu().removeIf(item -> item.getId() == id);

        atualizar(restaurante);
    }

    public MenuItem getItemPorId(Restaurante restaurante, int id) {
        for (MenuItem item : restaurante.getMenu()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void aceitarPedido(Restaurante restaurante, org.example.model.entity.Pedido pedido) {
        pedido.setAceiteRestaurante(true);
        pedido.setStatusPedido(StatusCorrida.EM_PREPARO);

        restaurante.getPedidosNotificados().removeIf(ped -> ped.getIdPedido() == pedido.getIdPedido());
        restaurante.getPedidosAceitos().add(pedido);
        atualizar(restaurante);
        pedidoRepository.atualizar(pedido);
        EntregadorService.atualizarPedidoNotificado(pedido);
        PassageiroService.atualizarPedidoNotificado(pedido);
    }

    public void rejeitarPedido(Restaurante restaurante, org.example.model.entity.Pedido pedido) {
        PedidoService pedidoService = new PedidoService();
        pedidoService.atualizarPedidosCancelados(pedido);
    }

    public static void receberAvaliacao(int idRestaurante, Double avaliacao){
        Restaurante restauranteAvaliado = buscarPorId(idRestaurante);

        if (restauranteAvaliado.getAvaliacao() == null){
            restauranteAvaliado.setAvaliacao(avaliacao);
        } else {
            Double novaAvaliacao = (restauranteAvaliado.getAvaliacao() + avaliacao)/2;
            restauranteAvaliado.setAvaliacao(novaAvaliacao);
        }

        atualizar(restauranteAvaliado);
    }
}
