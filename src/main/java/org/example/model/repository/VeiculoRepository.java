package org.example.model.repository;

import org.example.model.entity.Veiculo;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Veiculo.
 */
public class VeiculoRepository {

    private final JsonRepository<Veiculo> veiculosDB;

    public VeiculoRepository() {
        this.veiculosDB = new JsonRepository<>("src/main/resources/data/veiculos.json", Veiculo.class);
    }

    /**
     * Salva um novo veículo no arquivo JSON, gerando um ID robusto.
     * @param veiculo O novo veículo a ser salvo.
     */
    public void salvar(Veiculo veiculo) {
        List<Veiculo> veiculos = veiculosDB.carregar();
        int proximoId = veiculos.stream().mapToInt(Veiculo::getId).max().orElse(0) + 1;
        veiculo.setId(proximoId);
        veiculos.add(veiculo);
        veiculosDB.salvar(veiculos);
    }

    /**
     * Busca um veículo pelo seu ID.
     * @param id O ID do veículo a ser buscado.
     * @return O objeto Veiculo encontrado ou null.
     */
    public Veiculo buscarPorId(int id) {
        return veiculosDB.carregar().stream()
                .filter(v -> v.getId() == id)
                .findFirst().orElse(null);
    }

    /**
     * Busca um veículo pela sua placa.
     * @param placa A placa do veículo a ser buscada.
     * @return O objeto Veiculo encontrado ou null.
     */
    public Veiculo buscarPorPlaca(String placa) {
        return veiculosDB.carregar().stream()
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst().orElse(null);
    }
}