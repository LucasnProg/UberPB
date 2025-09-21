package org.example.model.service;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Veiculo;
import org.example.model.repository.VeiculoRepository;
import java.time.Year;

/**
 * Service com a lógica de negócio para a entidade Veiculo.
 */
public class VeiculoService {

    private final VeiculoRepository veiculoRepository = new VeiculoRepository();

    /**
     * Cadastra um novo veículo no sistema após validar a placa.
     * @param veiculo O objeto Veiculo com os dados preenchidos.
     * @return O objeto Veiculo com o ID gerado, ou null se o cadastro falhar.
     */
    public Veiculo cadastrar(Veiculo veiculo) {
        if (veiculoRepository.buscarPorPlaca(veiculo.getPlaca()) != null) {
            System.out.println("\n[ERRO] A placa '" + veiculo.getPlaca() + "' já está cadastrada no sistema.");
            return null;
        }
        veiculoRepository.salvar(veiculo);
        System.out.println("\nVeículo cadastrado com sucesso!");
        // Retorna o veículo com o ID preenchido para ser vinculado ao motorista
        return veiculoRepository.buscarPorPlaca(veiculo.getPlaca());
    }

    /**
     * Determina a categoria de um veículo com base em suas características.
     * A ordem de verificação prioriza as categorias mais exclusivas.
     * @param anoFabricacao Ano de fabricação do veículo.
     * @param numAssentos Número de assentos.
     * @param capacidadeMala Capacidade do porta-malas em litros.
     * @param premium Se o veículo é considerado premium.
     * @return A CategoriaVeiculo correspondente.
     */
    public CategoriaVeiculo determinarCategoria(int anoFabricacao, int numAssentos, float capacidadeMala, boolean premium) {
        int anoAtual = Year.now().getValue();

        if (premium) {
            return CategoriaVeiculo.UBER_BLACK;
        }

        if (numAssentos > 5) {
            return CategoriaVeiculo.UBER_XL;
        }

        if ((anoAtual - anoFabricacao) <= 6) {
            return CategoriaVeiculo.UBER_COMFORT;
        }

        if (capacidadeMala > 400) {
            return CategoriaVeiculo.UBER_BAG;
        }

        return CategoriaVeiculo.UBER_X;
    }

}