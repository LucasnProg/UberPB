package org.example.model.repository;

import org.example.model.entity.Corrida;
import java.util.List;
import java.util.ArrayList;

public class CorridaRepository implements Repository<Corrida> {

    private JsonRepository<Corrida> corridasDB = new JsonRepository<>("src/main/resources/data/corridas.json", Corrida.class);
    private List<Corrida> corridasCarregadas = corridasDB.carregar();

    public void salvarCorrida(Corrida corrida) {
        atualizarCorridasCarregadas();
        int currentId = corridasCarregadas.size() + 1;
        corrida.setId(currentId);
        corridasCarregadas.add(corrida);
        corridasDB.salvar(corridasCarregadas);
    }

    @Override
    public List<Corrida> carregar() {
        return corridasDB.carregar();
    }

    @Override
    public void salvar(List<Corrida> entidades) {
        corridasDB.salvar(entidades);
    }

    public List<Corrida> getCorridas() {
        atualizarCorridasCarregadas();
        return corridasCarregadas;
    }

    public Boolean existeCorrida(int idBusca) {
        atualizarCorridasCarregadas();
        for (Corrida c : corridasCarregadas) {
            if (c.getId() == idBusca) {
                return true;
            }
        }
        return false;
    }

    public Corrida buscarPorId(int idBusca) {
        atualizarCorridasCarregadas();
        for (Corrida c : corridasCarregadas) {
            if (c.getId() == idBusca) {
                return c;
            }
        }
        return null;
    }

    public void remover(int id) {
        atualizarCorridasCarregadas();
        corridasCarregadas.removeIf(c -> c.getId() == id);
        corridasDB.salvar(corridasCarregadas);
    }

    private void atualizarCorridasCarregadas() {
        this.corridasCarregadas = corridasDB.carregar();
    }
}