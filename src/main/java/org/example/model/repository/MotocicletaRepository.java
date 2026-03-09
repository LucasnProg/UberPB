package org.example.model.repository;

import org.example.model.entity.Motocicleta;

import java.util.List;

public class MotocicletaRepository{
    private final JsonRepository<Motocicleta> motosDB;

    public MotocicletaRepository() {
        this.motosDB = new JsonRepository<>("src/main/resources/data/motocicletas.json", Motocicleta.class);
    }

    public void salvar(Motocicleta moto) {
        List<Motocicleta> motos = motosDB.carregar();
        int proximoId = motos.stream().mapToInt(Motocicleta::getId).max().orElse(0) + 1;
        moto.setId(proximoId);
        motos.add(moto);
        motosDB.salvar(motos);
    }

    public Motocicleta buscarPorId(int id) {
        return motosDB.carregar().stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    public Motocicleta buscarPorPlaca(String placa) {
        return motosDB.carregar().stream().filter(v -> v.getPlaca().equalsIgnoreCase(placa)).findFirst().orElse(null);
    }
}
