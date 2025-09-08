package org.example.model.repository;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class VeiculoRepository implements Repository<Veiculo>{

    private static JsonRepository<Veiculo> veiculosDB = new JsonRepository<Veiculo>("src/main/resources/data/veiculos.json", Veiculo.class);
    private static List<Veiculo> veiculosCarregados = veiculosDB.carregar();

    public static void atualizarVeiculosCarregados() {
        veiculosCarregados = veiculosDB.carregar();
    }

    @Override
    public void salvar(List<Veiculo> entidades) {
        veiculosDB.salvar(entidades);
    }

    @Override
    public List<Veiculo> carregar() {
        return veiculosDB.carregar();
    }

    public static void salvarVeiculo(Veiculo veiculo){
        atualizarVeiculosCarregados();
        int currentId = veiculosCarregados.size() + 1;
        veiculo.setId(currentId);
        veiculosCarregados.add(veiculo);
        veiculosDB.salvar(veiculosCarregados);
    }

    public static Veiculo buscarPorId(int id){
        atualizarVeiculosCarregados();
        for(Veiculo v : veiculosCarregados){
            if(v.getId() == id) return v;
        }
        return null;
    }

    public static Veiculo buscarPorPlaca(String placa){
        atualizarVeiculosCarregados();
        for(Veiculo v : veiculosCarregados){
            if(v.getPlaca().equalsIgnoreCase(placa)) return v;
        }
        return null;
    }

    public static List<Veiculo> buscarPorCategoria(CategoriaVeiculo categoria){
        atualizarVeiculosCarregados();
        List<Veiculo> veiculosEncontrados = new ArrayList<>();

        for(Veiculo v : veiculosCarregados){
            if (v.getCategoria() == categoria) {
                veiculosEncontrados.add(v);
            }
        }
        return veiculosEncontrados;
    }


    public static boolean existePlaca(String placa){
        atualizarVeiculosCarregados();
        for (Veiculo v : veiculosCarregados) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return true;
            }
        }
        return false;
    }

    public List<Veiculo> getVeiculos() {
        atualizarVeiculosCarregados();
        return veiculosCarregados;
    }

    public void remover(int id) {
        atualizarVeiculosCarregados();
        for (Veiculo v : veiculosCarregados){
            if (v.getId() == id){
                veiculosCarregados.remove(v);
                veiculosDB.salvar(veiculosCarregados);
            }
        }
    }


}
