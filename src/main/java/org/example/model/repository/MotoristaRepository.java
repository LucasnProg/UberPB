package org.example.model.repository;

import org.example.model.entity.Motorista;

import java.util.List;

public class MotoristaRepository implements UsuarioRepository{

    private JsonRepository<Motorista> motoristasDB = new JsonRepository<Motorista>("src/main/resources/data/motoristas.json", Motorista.class);
    private List<Motorista> motoristasCarregados = motoristasDB.carregar();

    public void salvarMotorista(Motorista motorista) {
        atualizarMotoristasCarregados();
        int currentId = motoristasCarregados.size()+1;
        motorista.setId(currentId);
        motoristasCarregados.add(motorista);
        motoristasDB.salvar(motoristasCarregados);
    }


    @Override
    public Motorista buscarPorCpf(String cpfBusca) {
        atualizarMotoristasCarregados();

        for (Motorista m : motoristasCarregados){
            if (m.getCpf().equals(cpfBusca)){
                return m;
            }
        }

        return null;
    }

    @Override
    public void remover(String cpf) {
        atualizarMotoristasCarregados();
        for (Motorista m : motoristasCarregados){
            if (m.getCpf().equals(cpf)){
                motoristasCarregados.remove(m);
                motoristasDB.salvar(motoristasCarregados);
            }
        }
    }


    public List<Motorista> getMotoristas() {
        atualizarMotoristasCarregados();
        return motoristasCarregados;
    }

    public Boolean existeCpf(String cpfBusca){
        atualizarMotoristasCarregados();

        for (Motorista m : motoristasCarregados){
            if (m.getCpf().equals(cpfBusca)){
                return true;
            }
        }

        return false;
    }

    public void atualizarMotoristasCarregados(){
        this.motoristasCarregados = motoristasDB.carregar();
    }
}
