package org.example.model.service;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Veiculo;
import org.example.model.repository.VeiculoRepository;
import org.example.util.CrudVeiculoError;

import java.util.List;

public class VeiculoService {

    private final VeiculoRepository veiculoRepository = new VeiculoRepository();

    public VeiculoService(){}

    public void criar(String placa, String modelo, String marca, int ano, CategoriaVeiculo categoria, String cor) {
        try {
            if (VeiculoRepository.existePlaca(placa)) {
                throw new CrudVeiculoError("A placa '" + placa + "' já está cadastrada no sistema.");
            }

            Veiculo novoVeiculo = new Veiculo(marca, modelo, placa, ano, cor, categoria);
            VeiculoRepository.salvarVeiculo(novoVeiculo);
            System.out.println("Veículo cadastrado com sucesso!");

        } catch (CrudVeiculoError e) {
            System.err.println("Erro ao criar veículo: " + e.getMessage());
        }
    }

    public List<Veiculo> listar() {
        return veiculoRepository.getVeiculos();
    }

    public Veiculo buscarPorPlaca(String placa) {
        try {
            Veiculo veiculo = VeiculoRepository.buscarPorPlaca(placa);
            if (veiculo == null) {
                throw new CrudVeiculoError("Veículo com a placa '" + placa + "' não encontrado.");
            }
            return veiculo;
        } catch (CrudVeiculoError e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    public Veiculo buscarPorId(int id) {
        try {
            Veiculo veiculo = VeiculoRepository.buscarPorId(id);
            if (veiculo == null) {
                throw new CrudVeiculoError("Veículo com o id '" + id + "' não encontrado.");
            }
            return veiculo;
        } catch (CrudVeiculoError e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<Veiculo> buscarPorCategoria(CategoriaVeiculo categoria) {
        try {
            return VeiculoRepository.buscarPorCategoria(categoria);
        } catch (CrudVeiculoError e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
