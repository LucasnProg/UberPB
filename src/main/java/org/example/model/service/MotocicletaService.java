package org.example.model.service;

import org.example.model.entity.Motocicleta;
import org.example.model.entity.Veiculo;
import org.example.model.repository.MotocicletaRepository;

public class MotocicletaService {
    private final MotocicletaRepository motoRepository = new MotocicletaRepository();

    /**
     * Cadastra um novo veículo no sistema após validar a placa.
     * @param moto O objeto Veiculo com os dados preenchidos.
     * @return O objeto Veiculo com o ID gerado, ou null se o cadastro falhar.
     */
    public Motocicleta cadastrar(Motocicleta moto) {
        if (motoRepository.buscarPorPlaca(moto.getPlaca()) != null) {
            System.out.println("\n[ERRO] A placa '" + moto.getPlaca() + "' já está cadastrada no sistema.");
            return null;
        }
        motoRepository.salvar(moto);
        System.out.println("\nVeículo cadastrado com sucesso!");
        // Retorna o veículo com o ID preenchido para ser vinculado ao motorista
        return motoRepository.buscarPorPlaca(moto.getPlaca());
    }
}
