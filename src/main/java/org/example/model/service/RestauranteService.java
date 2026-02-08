package org.example.model.service;

import org.example.model.entity.Restaurante;
import org.example.model.repository.RestauranteRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Restaurantes.
 */
public class RestauranteService {

    private final RestauranteRepository restauranteRepository = new RestauranteRepository();

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
    public Restaurante criar(String nome, String email, String senha, String cnpj, String telefone) {
        if (restauranteRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }

        if (restauranteRepository.buscarPorCnpj(cnpj) != null) {
            System.out.println("\n[ERRO] O CNPJ informado já está cadastrado.");
            return null;
        }
        Restaurante novoRestaurante = new Restaurante(nome, email, senha, cnpj, telefone);
        restauranteRepository.salvar(novoRestaurante);
        System.out.println("\nRestaurante cadastrado com sucesso!");
        return novoRestaurante;
    }

    public Restaurante buscarPorId(int id) {
        return restauranteRepository.buscarPorId(id);
    }

    public void atualizar(Restaurante restaurante) {
        restauranteRepository.atualizar(restaurante);
    }
}
