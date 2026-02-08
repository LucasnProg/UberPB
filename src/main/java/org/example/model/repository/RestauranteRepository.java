package org.example.model.repository;

import org.example.model.entity.Restaurante;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para gerenciar a persistência de entidades Restaurante.
 */
public class RestauranteRepository {

    private final JsonRepository<Restaurante> restauranteDB;

    public RestauranteRepository() {
        this.restauranteDB = new JsonRepository<>("src/main/resources/data/restaurantes.json", Restaurante.class);
    }

    /**
     * Salva um novo restaurante no banco de dados.
     * Gera um novo ID automaticamente.
     * 
     * @param restaurante O restaurante a ser salvo.
     */
    public void salvar(Restaurante restaurante) {
        List<Restaurante> restaurantes = restauranteDB.carregar();
        int proximoId = restaurantes.stream().mapToInt(Restaurante::getId).max().orElse(0) + 1;
        restaurante.setId(proximoId);
        restaurantes.add(restaurante);
        restauranteDB.salvar(restaurantes);
    }

    /**
     * Atualiza os dados de um restaurante existente.
     * 
     * @param restauranteAtualizado O restaurante com os dados atualizados.
     */
    public void atualizar(Restaurante restauranteAtualizado) {
        List<Restaurante> restaurantes = restauranteDB.carregar();
        for (int i = 0; i < restaurantes.size(); i++) {
            if (restaurantes.get(i).getId() == restauranteAtualizado.getId()) {
                restaurantes.set(i, restauranteAtualizado);
                restauranteDB.salvar(restaurantes);
                return;
            }
        }
    }

    /**
     * Busca um restaurante pelo seu ID.
     * 
     * @param id O ID do restaurante.
     * @return O restaurante encontrado ou null se não existir.
     */
    public Restaurante buscarPorId(int id) {
        return restauranteDB.carregar().stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um restaurante pelo CNPJ.
     * 
     * @param cnpj O CNPJ do restaurante.
     * @return O restaurante encontrado ou null se não existir.
     */
    public Restaurante buscarPorCnpj(String cnpj) {
        return restauranteDB.carregar().stream()
                .filter(r -> r.getCnpj().equals(cnpj))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca um restaurante pelo Email.
     * @param email O email do restaurante.
     * @return O restaurante encontrado ou null se não existir.
     */
    public Restaurante buscarPorEmail(String email) {
        return restauranteDB.carregar().stream()
                .filter(r -> r.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
