package org.example.model.service;

import org.example.model.entity.Veiculo;
import org.example.model.repository.VeiculoRepository;

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
     * Deleta um veículo pelo seu ID. Útil para reverter um cadastro de motorista
     * que falhou após o veículo já ter sido criado.
     * @param id O ID do veículo a ser deletado.
     */
    public void deletar(int id) {
        // Implementação do método de deleção, se necessário
        // veiculoRepository.remover(id);
    }
}